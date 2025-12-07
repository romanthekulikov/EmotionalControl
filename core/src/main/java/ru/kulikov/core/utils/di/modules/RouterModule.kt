package ru.kulikov.core.utils.di.modules

import dagger.Module
import dagger.Provides
import ru.kulikov.core.utils.router.Router
import ru.kulikov.core.utils.router.RouterHolder

@Module
object RouterModule {

    @Provides
    fun provideRouter(): Router {
        return RouterHolder.router
    }
}