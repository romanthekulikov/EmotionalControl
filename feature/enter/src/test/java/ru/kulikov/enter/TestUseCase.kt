package ru.kulikov.enter

import com.roman_kulikov.tools.Result
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ru.kulikov.enter.data.EnterExceptionCatcher
import ru.kulikov.enter.domain.EnterRepository
import ru.kulikov.enter.domain.use_cases.impl.EnterUseCaseImpl

class TestUseCase {

    val repoMockk = mockk<EnterRepository>()
    val catcherMockk = EnterExceptionCatcher()

    @Before
    fun initTest() {
        every { runBlocking { repoMockk.enter(654323) } } returns Result.Success(true)
        every { runBlocking { repoMockk.enter(123456) } } returns Result.Failure("User not found")
    }

    @Test
    fun `enter case success`(): Unit = runBlocking {
        val useCase = EnterUseCaseImpl(repoMockk, catcherMockk)
        val result = useCase.invoke(654323)
        result as Result.Success
        result.data shouldBe true
    }

    @Test
    fun `enter case failure`(): Unit = runBlocking {
        val useCase = EnterUseCaseImpl(repoMockk, catcherMockk)
        val result = useCase.invoke(123456)
        result as Result.Failure
        result.cause shouldBe "User not found"
    }
}