package com.github.travelplannerapp.login

import dagger.Module
import dagger.Provides

/**
 * Define LoginActivity-specific dependencies here.
 */
@Module
class LoginModule {

    @Provides
    internal fun provideLoginPresenter(loginView: LoginContract.View): LoginPresenter {
        return LoginPresenter(loginView)
    }
}