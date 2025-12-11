package ru.kulikov.statistic

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
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import ru.kulikov.core.utils.data.AppSharedPreferencesImpl
import ru.kulikov.core.utils.data.EMOTIONAL_CHILD
import ru.kulikov.core.utils.data.INDICATORS_VALUE_CHILD
import ru.kulikov.core.utils.data.NAME_CHILD
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.statistic.data.StatisticRepositoryImpl
import java.time.LocalDate
import com.roman_kulikov.tools.Result
import junit.framework.TestCase.assertEquals
import ru.kulikov.core.utils.data.models.Indicator


@RunWith(AndroidJUnit4::class)
class StatisticRepositoryFirebaseTest {

    private lateinit var repo: StatisticRepositoryImpl
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

    private val testPartnerFirebaseId = "partner_user_789"
    private val testPartnerId = 654321
    private val testUserId = 123456

    @Before
    fun setup() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            prefs = AppSharedPreferencesImpl(context)
            repo = StatisticRepositoryImpl(prefs)

            // Очистка данных
            Firebase.database.reference.removeValue().await()

            // Создаем тестового партнера
            val partnerUser = Firebase.auth.createUserWithEmailAndPassword(
                "partner@test.com",
                "password456"
            ).await().user
            val actualPartnerFirebaseId = partnerUser!!.uid

            // Создаем структуру партнера в Realtime Database
            Firebase.database.reference
                .child("users")
                .child(actualPartnerFirebaseId)
                .apply {
                    child(USER_ID_CHILD).setValue(testPartnerId.toDouble())
                    child(NAME_CHILD).setValue("TestPartner")

                    // Добавляем тестовые данные для статистики
                    child(INDICATORS_VALUE_CHILD).apply {
                        // Данные за разные даты
                        child("2024-01-01").child(EMOTIONAL_CHILD).setValue(30.0)
                        child("2024-01-02").child(EMOTIONAL_CHILD).setValue(50.0)
                        child("2024-01-03").child(EMOTIONAL_CHILD).setValue(70.0)
                        child("2024-01-04").child(EMOTIONAL_CHILD).setValue(90.0)
                        child("2024-01-05").child(EMOTIONAL_CHILD).setValue(20.0)
                        // Дата без эмоционального показателя
                        child("2024-01-06").child("other_indicator").setValue(100.0)
                    }
                }
                //.await()

            // Создаем и авторизуем текущего пользователя (хотя он не используется напрямую)
            Firebase.auth.signOut()
            Firebase.auth.createUserWithEmailAndPassword(
                "test@test.com",
                "password123"
            ).await()
            Firebase.auth.signInWithEmailAndPassword("test@test.com", "password123").await()

            // Сохраняем partnerId в SharedPreferences
            prefs.savePartnerId(testPartnerId)
        }
    }

    @After
    fun tearDown() {
        Firebase.auth.signOut()
        prefs.forgotPartnerId()
    }

    @Test
    fun partnerStatisticByPeriod_returns_data_for_period() {
        runBlocking {
            val startDate = LocalDate.parse("2024-01-02")
            val endDate = LocalDate.parse("2024-01-04")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Должны быть данные только за 02, 03, 04 января
            assertEquals(3, indicatorValues.size)

            // Проверяем сортировку по дате
            assertEquals(LocalDate.parse("2024-01-02"), indicatorValues[0].date)
            assertEquals(LocalDate.parse("2024-01-03"), indicatorValues[1].date)
            assertEquals(LocalDate.parse("2024-01-04"), indicatorValues[2].date)

            // Проверяем значения
            assertEquals(50.0, (indicatorValues[0].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
            assertEquals(70.0, (indicatorValues[1].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
            assertEquals(90.0, (indicatorValues[2].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }

    @Test
    fun partnerStatisticByPeriod_returns_data_from_start_to_now_when_end_null() {
        runBlocking {
            val startDate = LocalDate.parse("2024-01-03")

            val result = repo.partnerStatisticByPeriod(startDate, null)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Должны быть данные от 03 января и далее
            assertEquals(3, indicatorValues.size) // 03, 04, 05 января

            // Проверяем даты
            assertEquals(LocalDate.parse("2024-01-03"), indicatorValues[0].date)
            assertEquals(LocalDate.parse("2024-01-04"), indicatorValues[1].date)
            assertEquals(LocalDate.parse("2024-01-05"), indicatorValues[2].date)
        }
    }

    @Test
    fun partnerStatisticByPeriod_skips_dates_without_emotional_indicator() {
        runBlocking {
            val startDate = LocalDate.parse("2024-01-05")
            val endDate = LocalDate.parse("2024-01-07")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Должен быть только 05 января (06-е без эмоционального показателя, 07-е нет данных)
            assertEquals(1, indicatorValues.size)
            assertEquals(LocalDate.parse("2024-01-05"), indicatorValues[0].date)
            assertEquals(20.0, (indicatorValues[0].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }

    @Test
    fun partnerStatisticByPeriod_returns_empty_statistic_when_no_data_in_period() {
        runBlocking {
            val startDate = LocalDate.parse("2024-02-01")
            val endDate = LocalDate.parse("2024-02-28")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Не должно быть данных за февраль
            assertTrue(indicatorValues.isEmpty())
        }
    }

    @Test
    fun partnerStatisticByPeriod_returns_single_day_when_start_equals_end() {
        runBlocking {
            val date = LocalDate.parse("2024-01-03")

            val result = repo.partnerStatisticByPeriod(date, date)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            assertEquals(1, indicatorValues.size)
            assertEquals(date, indicatorValues[0].date)
            assertEquals(70.0, (indicatorValues[0].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun partnerStatisticByPeriod_throws_when_partner_not_found() {
        runBlocking {
            // Устанавливаем несуществующий partnerId
            prefs.savePartnerId(999999)

            val startDate = LocalDate.parse("2024-01-01")
            val endDate = LocalDate.parse("2024-01-31")

            repo.partnerStatisticByPeriod(startDate, endDate)
        }
    }

    @Test
    fun partnerStatisticByPeriod_handles_dates_before_any_data() {
        runBlocking {
            val startDate = LocalDate.parse("2023-12-01")
            val endDate = LocalDate.parse("2023-12-31")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Не должно быть данных за декабрь 2023
            assertTrue(indicatorValues.isEmpty())
        }
    }

    @Test
    fun partnerStatisticByPeriod_handles_partial_overlap() {
        runBlocking {
            val startDate = LocalDate.parse("2024-01-04")
            val endDate = LocalDate.parse("2024-01-10")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            // Должны быть данные только за 04 и 05 января
            assertEquals(2, indicatorValues.size)
            assertEquals(LocalDate.parse("2024-01-04"), indicatorValues[0].date)
            assertEquals(LocalDate.parse("2024-01-05"), indicatorValues[1].date)
        }
    }

    @Test
    fun partnerStatisticByPeriod_returns_sorted_data() {
        runBlocking {
            // Добавляем данные в разном порядке
            val firebaseUserId = Firebase.auth.currentUser!!.uid

            // Добавляем данные за 2024-01-10 (это текущий пользователь, но для теста не важно)
            // В реальности нужно добавлять к партнеру
            Firebase.database.reference
                .child("users")
                .child(testPartnerFirebaseId)
                .child(INDICATORS_VALUE_CHILD)
                .apply {
                    // В произвольном порядке
                    child("2024-01-08").child(EMOTIONAL_CHILD).setValue(15.0)
                    child("2024-01-07").child(EMOTIONAL_CHILD).setValue(25.0)
                    child("2024-01-09").child(EMOTIONAL_CHILD).setValue(35.0)
                }
                //.await()

            val startDate = LocalDate.parse("2024-01-07")
            val endDate = LocalDate.parse("2024-01-09")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues
            // Проверяем сортировку по возрастанию даты
            assertEquals(LocalDate.parse("2024-01-07"), indicatorValues[0].date)
            assertEquals(LocalDate.parse("2024-01-08"), indicatorValues[1].date)
            assertEquals(LocalDate.parse("2024-01-09"), indicatorValues[2].date)
        }
    }

    @Test
    fun partnerStatisticByPeriod_handles_float_values() {
        runBlocking {
            // Добавляем данные с дробными значениями
            Firebase.database.reference
                .child("users")
                .child(testPartnerFirebaseId)
                .child(INDICATORS_VALUE_CHILD)
                .child("2024-01-15")
                .child(EMOTIONAL_CHILD)
                .setValue(45.75)
                .await()

            val startDate = LocalDate.parse("2024-01-15")
            val endDate = LocalDate.parse("2024-01-15")

            val result = repo.partnerStatisticByPeriod(startDate, endDate)

            assertTrue(result is Result.Success)
            val statistic = (result as Result.Success).data
            val indicatorValues = statistic.indicatorValues

            assertEquals(1, indicatorValues.size)
            assertEquals(45.75, (indicatorValues[0].indicators[0] as Indicator.EmotionalIndicator).percent, 0.001)
        }
    }
}