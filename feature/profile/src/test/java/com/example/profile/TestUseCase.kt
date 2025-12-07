package com.example.profile

import com.example.profile.domain.ProfileRepository
import com.example.profile.domain.use_cases.impl.GetUserUcImpl
import com.example.profile.domain.use_cases.impl.SaveUserUcImpl
import com.roman_kulikov.tools.Result
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ru.kulikov.core.utils.data.models.UserModel

class TestUseCase {

    private val repoMockk = mockk<ProfileRepository>()
    private val userMockk = UserModel(-1, "https://", "Name")

    @Before
    fun initMockk() {
        every { runBlocking { repoMockk.getUser() } } returns Result.Success(userMockk)
        every { runBlocking { repoMockk.saveUser(userMockk) } } returns Result.Success(true)
    }

    @Test
    fun `get user`(): Unit = runBlocking {
        val useCase = GetUserUcImpl(repoMockk)
        val result = useCase()
        result as Result.Success
        result.data.userId shouldBe -1
        result.data.avatarUrl shouldBe "https://"
        result.data.name shouldBe "Name"
    }

    @Test
    fun `save user`(): Unit = runBlocking {
        val useCase = SaveUserUcImpl(repoMockk)
        val result = useCase()
        result as Result.Success
        result.data shouldBe true
    }

}