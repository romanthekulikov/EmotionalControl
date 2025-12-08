package ru.kulikov.statistic.di.modules

import com.roman_kulikov.tools.ExceptionCatcher
import dagger.Binds
import dagger.Module
import ru.kulikov.statistic.data.StatisticExceptionCatcher
import ru.kulikov.statistic.data.StatisticRepositoryImpl
import ru.kulikov.statistic.domain.StatisticRepository
import ru.kulikov.statistic.domain.use_cases.FirstRecordDateUseCase
import ru.kulikov.statistic.domain.use_cases.PartnerStatisticByPeriodUseCase
import ru.kulikov.statistic.domain.use_cases.impl.FirstRecordDateUseCaseImpl
import ru.kulikov.statistic.domain.use_cases.impl.PartnerStatisticByPeriodUseCaseImpl
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
    fun bindFirstRecordDateUseCase(firstRecordDateUseCaseImpl: FirstRecordDateUseCaseImpl): FirstRecordDateUseCase

    @Singleton
    @Binds
    fun bindPartnerStatisticByPeriodUseCase(partnerStatisticByPeriodUseCaseImpl: PartnerStatisticByPeriodUseCaseImpl): PartnerStatisticByPeriodUseCase
}