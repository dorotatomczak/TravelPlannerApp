package com.github.travelplannerapp.di

import android.content.Context

import com.github.travelplannerapp.App

import dagger.Module
import dagger.Provides

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {

    @Provides
    internal fun provideContext(application: App): Context {
        return application.applicationContext
    }
}