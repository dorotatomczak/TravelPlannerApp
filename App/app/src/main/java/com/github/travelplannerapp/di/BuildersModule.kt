package com.github.travelplannerapp.di

import com.github.travelplannerapp.signIn.SignInActivity
import com.github.travelplannerapp.signIn.SignInModule
import com.github.travelplannerapp.signIn.SignInViewModel
import com.github.travelplannerapp.travels.TravelsActivity
import com.github.travelplannerapp.travels.TravelsModule
import com.github.travelplannerapp.travels.TravelsViewModel

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Binds all sub-components within the app.
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [SignInViewModel::class, SignInModule::class])
    internal abstract fun bindLoginActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [TravelsViewModel::class, TravelsModule::class])
    internal abstract fun bindTravelsActivity(): TravelsActivity

    // Add bindings for other sub-components here
}
