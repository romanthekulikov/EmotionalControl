package ru.kulikov.statistic.di.modules

import com.roman_kulikov.tools.ExceptionCatcher
import dagger.Binds
import dagger.Module
import ru.kulikov.statistic.data.StatisticExceptionCatcher
import ru.kulikov.statistic.data.StatisticRepositoryImpl
import ru.kulikov.statistic.domain.StatisticRepository
import ru.kulikov.statistic.domain.use_cases.ConvertStatisticToBarDataUseCase
import ru.kulikov.statistic.domain.use_cases.GetNextReferenceDateUseCase
import ru.kulikov.statistic.domain.use_cases.GetPeriodUseCase
import ru.kulikov.statistic.domain.use_cases.GetPreviousReferenceDayUseCase
import ru.kulikov.statistic.domain.use_cases.GetStatisticByPeriodUseCase
import ru.kulikov.statistic.domain.use_cases.impl.ConvertStatisticToBarDataUseCaseImpl
import ru.kulikov.statistic.domain.use_cases.impl.GetNextReferenceDateUseCaseImpl
import ru.kulikov.statistic.domain.use_cases.impl.GetPeriodUseCaseImpl
import ru.kulikov.statistic.domain.use_cases.impl.GetPreviousReferenceDayUseCaseImpl
import ru.kulikov.statistic.domain.use_cases.impl.GetStatisticByPeriodUseCaseImpl
import javax.inject.Singleton

@Module
interface StatisticModule {
    @Singleton
    @Binds
    fun provideExceptionCatcher(exceptionCatcher: StatisticExceptionCatcher): ExceptionCatcher

    @Singleton
    @Binds
    fun bindRepository(enterRepositoryImpl: StatisticRepositoryImpl): StatisticRepository

    @Singleton
    @Binds
    fun bindGetPeriodUseCase(getPeriodUseCase: GetPeriodUseCaseImpl): GetPeriodUseCase

    @Singleton
    @Binds
    fun bindGetStatisticByPeriodUseCase(getStatisticByPeriodUseCase: GetStatisticByPeriodUseCaseImpl): GetStatisticByPeriodUseCase

    @Singleton
    @Binds
    fun bindConvertStatisticToBarDataUseCase(convertStatisticToBarDataUseCase: ConvertStatisticToBarDataUseCaseImpl): ConvertStatisticToBarDataUseCase

    @Singleton
    @Binds
    fun bindGetNextReferenceDateUseCase(getNextReferenceDateUseCase: GetNextReferenceDateUseCaseImpl): GetNextReferenceDateUseCase

    @Singleton
    @Binds
    fun bindGetPreviousReferenceDayUseCase(getPreviousReferenceDayUseCase: GetPreviousReferenceDayUseCaseImpl): GetPreviousReferenceDayUseCase
}