package com.example.main.data

import com.example.main.domain.MainRepository
import com.google.firebase.database.getValue
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.data.Indicator
import ru.kulikov.core.utils.data.Indicator.Companion.EMOTIONAL_INDICATOR_NAME
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) : MainRepository {
    override suspend fun getUserIndicators(): Result<List<Indicator>> =
        Result.Success(getIndicators(AppFirebase.auth.currentUser!!.uid))

    override suspend fun getPartnerIndicators(): Result<List<Indicator>> {
        val partnerId = sharedPreferences.getPartnerId()
        val partnerFirebaseId = AppFirebase.usersReference
            .reference()
            .orderByChild(USER_ID_CHILD)
            .equalTo(partnerId.toDouble()).get().await().children.firstOrNull()?.key

        if (partnerFirebaseId == null) throw IllegalStateException()
        val t = getIndicators(partnerFirebaseId)
        return Result.Success(t)
    }

    override suspend fun saveIndicator(indicator: Indicator): Result<Boolean> {
        val userFirebaseId = AppFirebase.auth.currentUser!!.uid
        AppFirebase.usersReference
            .userReference(userFirebaseId)
            .indicatorsDataReference()
            .dateReference(getDateNow())
            .indicatorReference(indicator.name).setValue(indicator.percent)

        return Result.Success(true)
    }

    override fun forgotPartnerId() {
        sharedPreferences.forgotPartnerId()
    }

    private suspend fun getIndicators(firebaseUserId: String): List<Indicator> {
        val dateReference = AppFirebase.usersReference
            .userReference(firebaseUserId)
            .indicatorsDataReference()
            .dateReference(getDateNow())

        return buildList {
            dateReference.indicatorReference(EMOTIONAL_INDICATOR_NAME).get().await().getValue<Double>()?.let {
                add(Indicator.EmotionalIndicator(percent = it))
            }
        }
    }

    private fun getDateNow(): String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
}