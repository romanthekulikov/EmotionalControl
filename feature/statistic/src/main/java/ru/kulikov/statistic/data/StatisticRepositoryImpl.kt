package ru.kulikov.statistic.data

import com.google.firebase.database.getValue
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.data.EMOTIONAL_CHILD
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.data.models.Indicator
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.domain.UserId
import ru.kulikov.statistic.domain.IndicatorValue
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticRepository
import java.time.LocalDate
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) :
    StatisticRepository {

    override suspend fun partnerStatisticByPeriod(
        start: LocalDate,
        end: LocalDate?
    ): Result<Statistic> {
        val partnerId = sharedPreferences.getPartnerId()
        val partnerFirebaseId = getPartnerFirebaseId(partnerId)

        val userRef = AppFirebase.usersReference.userReference(partnerFirebaseId)
        val indicatorsRef = userRef.indicatorsDataReference()

        val snapshot = indicatorsRef.reference("").get().await()

        val indicatorValues = mutableListOf<IndicatorValue>()

        for (dateSnapshot in snapshot.children) {
            val dateString = dateSnapshot.key ?: continue

            val date = LocalDate.parse(dateString)

            if (date.isBefore(start)) continue
            if (end != null && date.isAfter(end)) continue

            val emotionalValue = dateSnapshot.child(EMOTIONAL_CHILD).getValue<Double>()

            if (emotionalValue != null) {
                val indicator = Indicator.EmotionalIndicator(
                    name = Indicator.EMOTIONAL_INDICATOR_NAME,
                    percent = emotionalValue
                )

                val indicatorValue = IndicatorValueImpl(
                    date = date,
                    indicators = listOf(indicator)
                )

                indicatorValues.add(indicatorValue)
            }
        }

        indicatorValues.sortBy { it.date }

        val statistic = StatisticImpl(indicatorValues)
        return Result.Success(statistic)
    }

    private suspend fun getPartnerFirebaseId(partnerId: UserId): String {
        val partnerFirebaseId = AppFirebase.usersReference
            .reference()
            .orderByChild(USER_ID_CHILD)
            .equalTo(partnerId.toDouble()).get().await().children.firstOrNull()?.key

        if (partnerFirebaseId == null) throw IllegalStateException()
        return partnerFirebaseId
    }
}