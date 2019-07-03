package com.github.travelplannerapp.signup

import dagger.Module
import dagger.Provides

/**
 * Define SignUpActivity-specific dependencies here.
 */
@Module
class SignUpModule {

    @Provides
    internal fun provideSignUpPresenter(signUpView: SignUpContract.View): SignUpContract.Presenter {
        return SignUpPresenter(signUpView)
    }
}