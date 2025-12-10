package ru.kulikov.statistic

import com.roman_kulikov.tools.Result
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.kulikov.core.utils.data.Indicator
import ru.kulikov.statistic.data.IndicatorValueImpl
import ru.kulikov.statistic.data.StatisticExceptionCatcher
import ru.kulikov.statistic.data.StatisticImpl
import ru.kulikov.statistic.domain.StatisticRepository
import java.time.LocalDate

class TestUseCases {
    val repoMock = mockk<StatisticRepository>()
    val catcher = StatisticExceptionCatcher()

    private val start = LocalDate.of(2023, 1, 1)
    private val end = LocalDate.of(2023, 1, 20)
    private val firstDate = LocalDate.of(2023, 1, 10)

    private val statistic = StatisticImpl(
        indicatorValues = listOf(
            IndicatorValueImpl(
                date = LocalDate.of(2023, 1, 1),
                indicators = listOf(
                    Indicator.EmotionalIndicator(percent = 45.0)
                )
            ),
            IndicatorValueImpl(
                date = LocalDate.of(2023, 1, 15),
                indicators = listOf(
                    Indicator.EmotionalIndicator(percent = 60.0)
                )
            )
        )
    )

    private val fullStatistic = StatisticImpl(
        indicatorValues = listOf(
            IndicatorValueImpl(
                date = LocalDate.of(2023, 1, 1),
                indicators = listOf(
                    Indicator.EmotionalIndicator(percent = 45.0)
                )
            ),
            IndicatorValueImpl(
                date = LocalDate.of(2023, 1, 15),
                indicators = listOf(
                    Indicator.EmotionalIndicator(percent = 60.0)
                )
            ),
            IndicatorValueImpl(
                date = LocalDate.of(2023, 1, 31),
                indicators = listOf(
                    Indicator.EmotionalIndicator(percent = 75.0)
                )
            )
        )
    )

    init {
        every { runBlocking { repoMock.firstRecordDate() } } returns Result.Success(firstDate)
        every {
            runBlocking {
                repoMock.partnerStatisticByPeriod(
                    start,
                    end
                )
            }
        } returns Result.Success(statistic)
        every {
            runBlocking {
                repoMock.partnerStatisticByPeriod(
                    start,
                    null
                )
            }
        } returns Result.Success(fullStatistic)
    }

    @Test
    fun `first record date case success`(): Unit = runBlocking {
        val useCase = FirstRecordDateUseCaseImpl(repoMock, catcher)
        val result = useCase()
        result as Result.Success
        result.data shouldBe firstDate
    }


    @Test
    fun `partner statistic by period case when end not null success`(): Unit = runBlocking {
        val useCase = PartnerStatisticByPeriodUseCaseImpl(repoMock, catcher)
        val result = useCase(start, end)
        result as Result.Success
        result.data shouldBe statistic
    }

    @Test
    fun `partner statistic by period case when end null success`(): Unit = runBlocking {
        val useCase = PartnerStatisticByPeriodUseCaseImpl(repoMock, catcher)
        val result = useCase(start, null)
        result as Result.Success
        result.data shouldBe fullStatistic
    }
}