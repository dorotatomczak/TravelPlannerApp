package com.github.travelplannerapp.signIn

import dagger.Binds
import dagger.Module

@Module
abstract class SignInViewModel {
    @Binds
    internal abstract fun provideSignInView(signInActivity: SignInActivity): SignInContract.View
}
