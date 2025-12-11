package com.example.main

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.main.data.MainRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import org.junit.BeforeClass
import org.junit.runner.RunWith
import ru.kulikov.core.utils.domain.AppSharedPreferences
import com.roman_kulikov.tools.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.kulikov.core.utils.data.AppSharedPreferencesImpl
import ru.kulikov.core.utils.data.INDICATORS_VALUE_CHILD
import ru.kulikov.core.utils.data.NAME_CHILD
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.data.models.Indicator
import ru.kulikov.core.utils.data.models.Indicator.Companion.EMOTIONAL_INDICATOR_NAME
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class MainRepositoryFirebaseTest {

    private lateinit var repo: MainRepositoryImpl
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

    private val testCurrentUserFirebaseId = "current_user_456"
    private val testPartnerUserFirebaseId = "partner_user_789"
    private val testCurrentUserId = 123456
    private val testPartnerId = 654321
    private val testCurrentUserName = "CurrentUserName"
    private val testPartnerName = "PartnerName"

    @Before
    fun setup() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            prefs = AppSharedPreferencesImpl(context)
            repo = MainRepositoryImpl(prefs)

            // Очистка данных
            Firebase.database.reference.removeValue().await()

            // Создаем тестового текущего пользователя в Firebase Auth
            Firebase.auth.signOut()
            val currentUser = Firebase.auth.createUserWithEmailAndPassword(
                "test@test.com",
                "password123"
            ).await().user
            // Не можем изменить UID, поэтому используем существующий
            val actualCurrentUserFirebaseId = currentUser!!.uid

            // Создаем тестового партнера
            val partnerUser = Firebase.auth.createUserWithEmailAndPassword(
                "partner@test.com",
                "password456"
            ).await().user
            val actualPartnerFirebaseId = partnerUser!!.uid

            // Создаем структуру пользователей в Realtime Database
            // Текущий пользователь
            Firebase.database.reference
                .child("users")
                .child(actualCurrentUserFirebaseId)
                .apply {
                    child(USER_ID_CHILD).setValue(testCurrentUserId.toDouble())
                    child(NAME_CHILD).setValue(testCurrentUserName)
                }
                //.await()

            // Партнер
            Firebase.database.reference
                .child("users")
                .child(actualPartnerFirebaseId)
                .apply {
                    child(USER_ID_CHILD).setValue(testPartnerId.toDouble())
                    child(NAME_CHILD).setValue(testPartnerName)
                }
                //.await()

            // Сохраняем partnerId в SharedPreferences
            prefs.savePartnerId(testPartnerId)

            // Авторизуемся как текущий пользователь
            Firebase.auth.signInWithEmailAndPassword("test@test.com", "password123").await()
        }
    }

    @After
    fun tearDown() {
        Firebase.auth.signOut()
        prefs.forgotPartnerId()
    }

    @Test
    fun getUserIndicators_returns_default_indicators_when_no_data() {
        runBlocking {
            val result = repo.getUserIndicators()

            assertTrue(result is Result.Success)
            val indicators = (result as Result.Success).data
            assertEquals(1, indicators.size)
            assertTrue(indicators[0] is Indicator.EmotionalIndicator)
            assertEquals(1.0, (indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }

    @Test
    fun getPartnerIndicators_returns_success_when_partner_exists() {
        runBlocking {
            val result = repo.getPartnerIndicators()

            assertTrue(result is Result.Success)
            val indicators = (result as Result.Success).data
            assertEquals(1, indicators.size)
            assertTrue(indicators[0] is Indicator.EmotionalIndicator)
            assertEquals(1.0, (indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun getPartnerIndicators_throws_when_partner_not_found() {
        runBlocking {
            // Удаляем partnerId из SharedPreferences
            prefs.forgotPartnerId()

            repo.getPartnerIndicators()
        }
    }

    @Test
    fun saveIndicator_saves_data_successfully() {
        runBlocking {
            val indicator = Indicator.EmotionalIndicator(percent = 75.5)
            val result = repo.saveIndicator(indicator)

            assertTrue(result is Result.Success)
            assertEquals(true, (result as Result.Success).data)

            // Проверяем, что данные сохранились в Firebase
            val currentUser = Firebase.auth.currentUser
            assertNotNull(currentUser)

            val savedValue = Firebase.database.reference
                .child("users")
                .child(currentUser!!.uid)
                .child(INDICATORS_VALUE_CHILD)
                .child(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .child(EMOTIONAL_INDICATOR_NAME)
                .get()
                .await()
                .getValue<Double>()

            assertEquals(75.5, savedValue!!, 0.001)
        }
    }

    @Test
    fun forgotPartnerId_removes_partner_id_from_prefs() {
        runBlocking {
            // Предварительно убеждаемся, что partnerId есть
            assertEquals(testPartnerId, prefs.getPartnerId())

            repo.forgotPartnerId()

            // Проверяем, что partnerId удален
            assertFalse(prefs.getPartnerId() != null && prefs.getPartnerId() != 0)
        }
    }

    @Test
    fun getUserName_returns_current_user_name() {
        runBlocking {
            val result = repo.getUserName()

            assertTrue(result is Result.Success)
            assertEquals(testCurrentUserName, (result as Result.Success).data)
        }
    }

    @Test
    fun getPartnerName_returns_partner_name_when_partner_exists() {
        runBlocking {
            val result = repo.getPartnerName()

            assertTrue(result is Result.Success)
            assertEquals(testPartnerName, (result as Result.Success).data)
        }
    }

    @Test
    fun getPartnerName_returns_empty_string_when_partner_not_found() {
        runBlocking {
            // Удаляем partnerId
            prefs.forgotPartnerId()

            val result = repo.getPartnerName()

            assertTrue(result is Result.Success)
            assertEquals("", (result as Result.Success).data)
        }
    }

    @Test
    fun getIndicators_returns_saved_data_when_exists() {
        runBlocking {
            // Сначала сохраняем показатель
            val indicator = Indicator.EmotionalIndicator(percent = 88.0)
            repo.saveIndicator(indicator)

            // Затем получаем показатели
            val result = repo.getUserIndicators()

            assertTrue(result is Result.Success)
            val indicators = (result as Result.Success).data
            assertEquals(1, indicators.size)
            assertEquals(88.0, (indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }
}