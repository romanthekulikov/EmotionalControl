package com.example.main

import com.example.main.domain.MainRepository
import com.example.main.domain.use_cases.impl.GetPartnerIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.GetUserIndicatorsUcImpl
import com.example.main.domain.use_cases.impl.SaveIndicatorUcImpl
import com.roman_kulikov.tools.Result
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.kulikov.core.utils.data.Indicator

class TestUseCases {
    val repoMock = mockk<MainRepository>()

    init {
        every { repoMock.getUserIndicators() } returns Result.Success(listOf(Indicator.EmotionalIndicator(percent = 67.5)))
        every { repoMock.getPartnerIndicators() } returns Result.Success(listOf(Indicator.EmotionalIndicator(percent = 62.5)))
        every { repoMock.saveIndicator(Indicator.EmotionalIndicator(percent = 43.5)) } returns Result.Success(true)
    }

    @Test
    fun `get user indicators`(): Unit = runBlocking {
        val getUserIndicatorsUc = GetUserIndicatorsUcImpl(repoMock)
        val result = getUserIndicatorsUc()
        (result as Result.Success).data.size shouldBe 1
        result.data[0] as Indicator.EmotionalIndicator
        (result.data[0] as Indicator.EmotionalIndicator).percent shouldBe 67.5
    }

    @Test
    fun `get partner indicators`(): Unit = runBlocking {
        val getPartnerIndicatorsUc = GetPartnerIndicatorsUcImpl(repoMock)
        val result = getPartnerIndicatorsUc()
        (result as Result.Success).data.size shouldBe 1
        result.data[0] as Indicator.EmotionalIndicator
        (result.data[0] as Indicator.EmotionalIndicator).percent shouldBe 62.5
    }

    @Test
    fun `save indicator`(): Unit = runBlocking {
        val saveIndicatorUc = SaveIndicatorUcImpl(repoMock)
        val result = saveIndicatorUc()
        (result as Result.Success).data shouldBe true
    }
}