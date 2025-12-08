package ru.kulikov.statistic.ui

import com.roman_kulikov.tools.Result
import ru.kulikov.statistic.di.StatisticComponent
import ru.kulikov.statistic.domain.Statistic
import ru.kulikov.statistic.domain.StatisticContract
import ru.kulikov.statistic.domain.use_cases.FirstRecordDateUseCase
import ru.kulikov.statistic.domain.use_cases.PartnerStatisticByPeriodUseCase
import java.time.LocalDate
import javax.inject.Inject

class StatisticViewModel : StatisticStateHandler(), StatisticContract {

    @Inject
    lateinit var firstRecordDateUseCase: FirstRecordDateUseCase

    @Inject
    lateinit var partnerStatisticByPeriodUseCase: PartnerStatisticByPeriodUseCase

    override fun partnerStatisticByPeriod(
        start: LocalDate,
        end: LocalDate?
    ): Result<Statistic> {
        TODO("Not yet implemented")
    }

    init {
        StatisticComponent.getInstance().inject(this)
    }

    override fun firstRecordDate(): Result<LocalDate> {
        TODO("Not yet implemented")
    }
}