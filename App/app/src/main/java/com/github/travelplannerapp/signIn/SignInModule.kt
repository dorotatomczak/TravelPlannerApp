package com.github.travelplannerapp.signIn

import dagger.Module
import dagger.Provides

/**
 * Define SignInActivity-specific dependencies here.
 */
@Module
class SignInModule {

    @Provides
    internal fun provideSignInPresenter(signInView: SignInContract.View): SignInContract.Presenter {
        return SignInPresenter(signInView)
    }
}