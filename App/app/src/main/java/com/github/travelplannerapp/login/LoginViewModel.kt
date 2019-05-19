package com.github.travelplannerapp.login

import dagger.Binds
import dagger.Module

@Module
abstract class LoginViewModel {
    @Binds
    internal abstract fun provideLoginView(loginActivity: LoginActivity): LoginContract.View
}
