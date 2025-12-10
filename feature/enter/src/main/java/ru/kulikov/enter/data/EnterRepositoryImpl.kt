package ru.kulikov.enter.data

import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.enter.domain.EnterRepository
import javax.inject.Inject

class EnterRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) : EnterRepository {
    override suspend fun enter(partnerId: Int): Result<Boolean> {
        val partnerFirebaseId = getPartnerFirebaseId(partnerId)
        if (partnerFirebaseId == null) throw IllegalStateException()
        sharedPreferences.savePartnerId(partnerId)

        return Result.Success(true)
    }

    override fun getUserId(): Result<Int> = Result.Success(sharedPreferences.getUserId())

    override fun forgotUser() {
        sharedPreferences.forgotUserId()
    }

    private suspend fun getPartnerFirebaseId(partnerId: Int): String? {
        return AppFirebase.usersReference
            .reference()
            .orderByChild(USER_ID_CHILD)
            .equalTo(partnerId.toDouble()).get().await().children.firstOrNull()?.key
    }
}