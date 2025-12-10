package com.example.profile.di

import com.example.profile.di.modules.ProfileModule
import com.example.profile.ui.ProfileActivity
import com.example.profile.ui.ProfileViewModel
import dagger.Component
import ru.kulikov.core.utils.di.CoreComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [ProfileModule::class], dependencies = [CoreComponent::class])
interface ProfileComponent {

    @Component.Builder
    interface Builder {
        fun coreComponent(coreComponent: CoreComponent): Builder
        fun build(): ProfileComponent
    }

    fun inject(activity: ProfileActivity)
    fun inject(viewModel: ProfileViewModel)

    companion object {
        private var instance: ProfileComponent? = null

        fun init(coreComponent: CoreComponent): ProfileComponent {
            if (instance == null) {
                instance = DaggerProfileComponent.builder().coreComponent(coreComponent).build()
            }

            return instance!!
        }

        fun getInstance(): ProfileComponent = if (instance != null) instance!! else throw UninitializedPropertyAccessException()
    }
}