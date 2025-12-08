package ru.kulikov.enter.data

import com.roman_kulikov.tools.Result
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.enter.domain.EnterRepository
import javax.inject.Inject

class EnterRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) : EnterRepository {
    override suspend fun enter(partnerId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}