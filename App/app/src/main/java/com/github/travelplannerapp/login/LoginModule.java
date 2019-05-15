package com.github.travelplannerapp.login;

import dagger.Module;
import dagger.Provides;

/**
 * Define LoginActivity-specific dependencies here.
 */
@Module
public class LoginModule {

    @Provides
    LoginPresenter provideLoginPresenter(LoginContract.View loginView) {
        return new LoginPresenter(loginView);
    }
}