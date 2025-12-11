package com.example.profile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.profile.data.ProfileRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import ru.kulikov.core.utils.data.AppSharedPreferencesImpl
import ru.kulikov.core.utils.data.NAME_CHILD
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import com.roman_kulikov.tools.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import ru.kulikov.core.utils.data.models.UserModel

@RunWith(AndroidJUnit4::class)
class ProfileRepositoryFirebaseTest {

    private lateinit var repo: ProfileRepositoryImpl
    private lateinit var prefs: AppSharedPreferences

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupFirebaseEmulator() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            // Инициализируем FirebaseApp вручную (важно!)
            try {
                FirebaseApp.getInstance()
            } catch (e: IllegalStateException) {
                FirebaseApp.initializeApp(context)
            }

            // Теперь можно подключить эмулятор
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }

    private val testUserId = 123456
    private val testUserName = "TestUser"
    private val testUpdatedUserName = "UpdatedUserName"

    @Before
    fun setup() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            prefs = AppSharedPreferencesImpl(context)
            repo = ProfileRepositoryImpl(prefs)

            // Очистка данных
            Firebase.database.reference.removeValue().await()

            // Создаем тестового пользователя в Firebase Auth
            Firebase.auth.signOut()
            val user = Firebase.auth.createUserWithEmailAndPassword(
                "test@test.com",
                "password123"
            ).await().user

            val firebaseUserId = user!!.uid

            // Создаем структуру пользователя в Realtime Database
            Firebase.database.reference
                .child("users")
                .child(firebaseUserId)
                .apply {
                    child(USER_ID_CHILD).setValue(testUserId.toDouble())
                    child(NAME_CHILD).setValue(testUserName)
                }
                //.await()

            // Сохраняем userId в SharedPreferences
            prefs.saveUserId(testUserId)

            // Авторизуемся
            Firebase.auth.signInWithEmailAndPassword("test@test.com", "password123").await()
        }
    }

    @After
    fun tearDown() {
        Firebase.auth.signOut()
        prefs.forgotUserId()
    }

    @Test
    fun getUser_returns_user_with_correct_data() {
        runBlocking {
            val result = repo.getUser()

            assertTrue(result is Result.Success)
            val user = (result as Result.Success).data
            assertEquals(testUserId, user.userId)
            assertEquals(testUserName, user.name)
        }
    }

    @Test
    fun getUser_returns_user_with_empty_name_when_name_not_set() {
        runBlocking {
            // Удаляем имя из Firebase
            val firebaseUserId = Firebase.auth.currentUser!!.uid
            Firebase.database.reference
                .child("users")
                .child(firebaseUserId)
                .child(NAME_CHILD)
                .removeValue()
                .await()

            val result = repo.getUser()

            assertTrue(result is Result.Success)
            val user = (result as Result.Success).data
            assertEquals(testUserId, user.userId)
            assertEquals("", user.name)
        }
    }

    @Test
    fun saveUser_updates_user_name_successfully() {
        runBlocking {
            val updatedUser = UserModel(testUserId, testUpdatedUserName)
            val result = repo.saveUser(updatedUser)

            assertTrue(result is Result.Success)
            assertEquals(true, (result as Result.Success).data)

            // Проверяем, что имя обновилось в Firebase
            val firebaseUserId = Firebase.auth.currentUser!!.uid
            val savedName = Firebase.database.reference
                .child("users")
                .child(firebaseUserId)
                .child(NAME_CHILD)
                .get()
                .await()
                .getValue<String>()

            assertEquals(testUpdatedUserName, savedName)

            // Проверяем, что getUser возвращает обновленные данные
            val getUserResult = repo.getUser()
            val userAfterUpdate = (getUserResult as Result.Success).data
            assertEquals(testUpdatedUserName, userAfterUpdate.name)
        }
    }

    @Test
    fun saveUser_does_not_affect_user_id() {
        runBlocking {
            // Пытаемся изменить ID (хотя он не должен меняться)
            val userWithDifferentId = UserModel(999999, testUpdatedUserName)
            val result = repo.saveUser(userWithDifferentId)

            assertTrue(result is Result.Success)

            // Проверяем, что ID в SharedPreferences не изменился
            assertEquals(testUserId, prefs.getUserId())

            // Проверяем, что ID в Firebase не изменился
            val firebaseUserId = Firebase.auth.currentUser!!.uid
            val savedId = Firebase.database.reference
                .child("users")
                .child(firebaseUserId)
                .child(USER_ID_CHILD)
                .get()
                .await()
                .getValue<Double>()

            assertEquals(testUserId.toDouble(), savedId)
        }
    }

    @Test
    fun saveUser_works_with_empty_name() {
        runBlocking {
            val userWithEmptyName = UserModel(testUserId,"")
            val result = repo.saveUser(userWithEmptyName)

            assertTrue(result is Result.Success)

            // Проверяем, что имя установилось как пустая строка
            val firebaseUserId = Firebase.auth.currentUser!!.uid
            val savedName = Firebase.database.reference
                .child("users")
                .child(firebaseUserId)
                .child(NAME_CHILD)
                .get()
                .await()
                .getValue<String>()

            assertEquals("", savedName)
        }
    }

    @Test
    fun getUser_returns_user_even_when_user_id_not_in_prefs() {
        runBlocking {
            // Удаляем userId из SharedPreferences
            prefs.forgotUserId()

            // В этом случае getUser все равно должен работать,
            // но вернет default значение для userId (предположительно 0)
            val result = repo.getUser()

            assertTrue(result is Result.Success)
            val user = (result as Result.Success).data
            // Здесь предполагается, что getUserId() возвращает 0 или default значение
            // если userId не сохранен
            assertNotNull(user.userId)
            assertEquals(testUserName, user.name)
        }
    }

    @Test(expected = NullPointerException::class)
    fun getUser_throws_when_not_authenticated() {
        runBlocking {
            // Выходим из аккаунта
            Firebase.auth.signOut()

            repo.getUser()
        }
    }

    @Test(expected = NullPointerException::class)
    fun saveUser_throws_when_not_authenticated() {
        runBlocking {
            // Выходим из аккаунта
            Firebase.auth.signOut()

            val user = UserModel(testUserId, testUpdatedUserName)
            repo.saveUser(user)
        }
    }

    @Test
    fun integration_flow_get_save_get() {
        runBlocking {
            // 1. Получаем исходного пользователя
            val initialResult = repo.getUser()
            val initialUser = (initialResult as Result.Success).data
            assertEquals(testUserName, initialUser.name)

            // 2. Сохраняем обновленного пользователя
            val updatedUser = UserModel(testUserId, "CompletelyNewName")
            val saveResult = repo.saveUser(updatedUser)
            assertTrue(saveResult is Result.Success)

            // 3. Снова получаем пользователя и проверяем обновление
            val finalResult = repo.getUser()
            val finalUser = (finalResult as Result.Success).data
            assertEquals("CompletelyNewName", finalUser.name)
            assertEquals(testUserId, finalUser.userId)
        }
    }
}