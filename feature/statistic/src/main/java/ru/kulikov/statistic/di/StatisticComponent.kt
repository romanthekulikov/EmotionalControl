package ru.kulikov.statistic.di

import dagger.Component
import ru.kulikov.core.utils.di.CoreComponent
import ru.kulikov.statistic.di.modules.StatisticModule
import ru.kulikov.statistic.ui.StatisticActivity
import ru.kulikov.statistic.ui.StatisticViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [StatisticModule::class], dependencies = [CoreComponent::class])
interface StatisticComponent {
    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): StatisticComponent
    }


    fun inject(viewModel: StatisticViewModel)
    fun inject(activity: StatisticActivity)
    companion object {
        private var instance: StatisticComponent? = null

        fun init(coreComponent: CoreComponent): StatisticComponent {
            if (instance == null) {
                instance = DaggerStatisticComponent.builder().coreComponent(coreComponent).build()
            }

            return instance!!
        }

        fun getInstance(): StatisticComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}