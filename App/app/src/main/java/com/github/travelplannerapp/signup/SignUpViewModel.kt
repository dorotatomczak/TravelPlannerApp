package com.github.travelplannerapp.signup

import dagger.Binds
import dagger.Module

@Module
abstract class SignUpViewModel {
    @Binds
    internal abstract fun provideSignUpView(signUpActivity: SignUpActivity): SignUpContract.View
}
