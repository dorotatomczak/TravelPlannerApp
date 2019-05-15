package com.github.travelplannerapp.login;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginViewModel {
    @Binds
    abstract LoginContract.View provideLoginView(LoginActivity loginActivity);
}
