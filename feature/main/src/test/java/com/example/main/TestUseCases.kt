package com.example.main

import com.example.main.data.MainExceptionCatcher
import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.impl.GetEmojiUcImpl
import com.example.main.domain.use_cases.impl.GetPartnerIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.GetUserIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.SaveIndicatorUcImpl
import com.roman_kulikov.tools.Result
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.kulikov.core.utils.data.models.Indicator

class TestUseCases {
    val repoMock = mockk<MainRepository>()
    val catcherMockk = MainExceptionCatcher()
    val indicator = Indicator.EmotionalIndicator(percent = 34.3)

    init {
        every { runBlocking { repoMock.getUserIndicators() } } returns Result.Success(listOf(Indicator.EmotionalIndicator(percent = 67.5)))
        every { runBlocking { repoMock.getPartnerIndicators() } } returns Result.Success(
            listOf(
                Indicator.EmotionalIndicator(
                    percent = 62.5
                )
            )
        )
        every { runBlocking { repoMock.saveIndicator(indicator) } } returns Result.Success(true)
    }

    @Test
    fun `get user indicators`(): Unit = runBlocking {
        val getUserIndicatorsUc = GetUserIndicatorsUcImpl(repoMock, catcherMockk)
        val result = getUserIndicatorsUc()
        (result as Result.Success).data.size shouldBe 1
        result.data[0] as Indicator.EmotionalIndicator
        (result.data[0] as Indicator.EmotionalIndicator).percent shouldBe 67.5
    }

    @Test
    fun `get partner indicators`(): Unit = runBlocking {
        val getPartnerIndicatorsUc = GetPartnerIndicatorsUcImpl(repoMock, catcherMockk)
        val result = getPartnerIndicatorsUc()
        (result as Result.Success).data.size shouldBe 1
        result.data[0] as Indicator.EmotionalIndicator
        (result.data[0] as Indicator.EmotionalIndicator).percent shouldBe 62.5
    }

    @Test
    fun `save indicator`(): Unit = runBlocking {
        val saveIndicatorUc = SaveIndicatorUcImpl(repoMock, catcherMockk)
        val result = saveIndicatorUc(indicator)
        (result as Result.Success).data shouldBe true
    }

    @Test
    fun `get emoji resource id`() {
        val useCase = GetEmojiUcImpl()
        useCase(0.0) shouldBe R.drawable.ic_angry_emotional
        useCase(0.20) shouldBe R.drawable.ic_angry_emotional
        useCase(0.21) shouldBe R.drawable.ic_sad_emotional
        useCase(0.40) shouldBe R.drawable.ic_sad_emotional
        useCase(0.41) shouldBe R.drawable.ic_medium_emotional
        useCase(0.60) shouldBe R.drawable.ic_medium_emotional
        useCase(0.61) shouldBe R.drawable.ic_good_emotional
        useCase(0.80) shouldBe R.drawable.ic_good_emotional
        useCase(0.81) shouldBe R.drawable.ic_max_emotional
        useCase(1.0) shouldBe R.drawable.ic_max_emotional
    }
}