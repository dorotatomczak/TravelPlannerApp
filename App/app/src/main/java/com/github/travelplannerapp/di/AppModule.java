package com.github.travelplannerapp.di;

import android.content.Context;

import com.github.travelplannerapp.App;

import dagger.Module;
import dagger.Provides;

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }
}