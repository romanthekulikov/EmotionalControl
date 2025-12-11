package ru.kulikov.enter

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.kulikov.core.utils.data.AppSharedPreferencesImpl
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.enter.data.EnterRepositoryImpl
import com.roman_kulikov.tools.Result
import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass

@RunWith(AndroidJUnit4::class)
class EnterRepositoryFirebaseTest {

    private lateinit var repo: EnterRepositoryImpl
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

    private val testUserFirebaseKey = "test_user_123"
    private val testUserId = 654323

    @Before
    fun setup() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            prefs = AppSharedPreferencesImpl(context)
            repo = EnterRepositoryImpl(prefs)

            // Полная очистка users в эмуляторе
            //Firebase.database.reference.child("users").removeValue().await()

            // Добавляем тестового пользователя
            Firebase.database.reference
                .child("users")
                .child(testUserFirebaseKey)
                .child(USER_ID_CHILD)
                .setValue(testUserId.toDouble())
                .await()
        }
    }

    @Test
    fun enter_returns_success_when_user_exists() {
        runBlocking {
            val result = repo.enter(testUserId)

            assertTrue(result is Result.Success)
            assertEquals(true, (result as Result.Success).data)

            // Проверяем SharedPreferences
            assertEquals(testUserId, prefs.getPartnerId())
        }
    }

    @Test(expected = IllegalStateException::class)
    fun enter_throws_when_user_does_not_exist() {
        runBlocking {
            repo.enter(111111) // такого ID нет → должен быть IllegalStateException
        }
    }
}
