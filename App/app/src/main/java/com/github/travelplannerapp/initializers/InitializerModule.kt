package com.github.travelplannerapp.initializers

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class InitializerModule {
    @Binds
    @IntoSet
    abstract fun sharedPreferencesInitializer(
            sharedPreferencesInitializer: SharedPreferencesInitializer
    ): Initializer
}