package com.github.travelplannerapp.di

import android.content.Context

import com.github.travelplannerapp.App

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: App): Context = application.applicationContext

}