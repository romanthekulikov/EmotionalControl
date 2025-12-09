package ru.kulikov.statistic.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.getValue
import com.roman_kulikov.tools.Result
import kotlinx.coroutines.tasks.await
import ru.kulikov.core.utils.data.AppFirebase
import ru.kulikov.core.utils.data.EMOTIONAL_CHILD
import ru.kulikov.core.utils.data.Indicator
import ru.kulikov.core.utils.data.USER_ID_CHILD
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.domain.UserId
import ru.kulikov.statistic.domain.IndicatorValue
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticRepository
import java.time.LocalDate
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(private val sharedPreferences: AppSharedPreferences) :
    StatisticRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun partnerStatisticByPeriod(
        start: LocalDate,
        end: LocalDate?
    ): Result<Statistic> {
        val partnerId = sharedPreferences.getPartnerId();
        val partnerFirebaseId = getPartnerFirebaseId(partnerId);

        val userRef = AppFirebase.usersReference.userReference(partnerFirebaseId)
        val indicatorsRef = userRef.indicatorsDataReference()

        val snapshot = indicatorsRef.reference("").get().await()

        val indicatorValues = mutableListOf<IndicatorValue>()

        for (dateSnapshot in snapshot.children) {
            val dateString = dateSnapshot.key ?: continue

            val dateParts = dateString.split("-")
            if (dateParts.size != 3) continue

            val date = try {
                LocalDate.of(
                    dateParts[2].toInt(),
                    dateParts[1].toInt(),
                    dateParts[0].toInt()
                )
            } catch (e: Exception) {
                continue
            }

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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun firstRecordDate(): Result<LocalDate> {
        val partnerId = sharedPreferences.getPartnerId();
        val partnerFirebaseId = getPartnerFirebaseId(partnerId);

        val userRef = AppFirebase.usersReference.userReference(partnerFirebaseId)
        val indicatorsRef = userRef.indicatorsDataReference()

        val snapshot = indicatorsRef.reference("").get().await()

        if (!snapshot.exists() || snapshot.childrenCount == 0L) {
            throw IllegalStateException("No indicator")
        }

        val earliestDate = snapshot.children
            .mapNotNull { dateSnapshot ->
                dateSnapshot.key?.let { dateString ->
                    try {
                        val parts = dateString.split("-")
                        if (parts.size != 3) return@let null
                        LocalDate.of(
                            parts[2].toInt(),
                            parts[1].toInt(),
                            parts[0].toInt()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            .minOrNull()
            ?: throw IllegalStateException("Date not found")

        return Result.Success(earliestDate)
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