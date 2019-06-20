package com.github.travelplannerapp.di

import com.github.travelplannerapp.addtravel.AddTravelActivity
import com.github.travelplannerapp.addtravel.AddTravelModule
import com.github.travelplannerapp.addtravel.AddTravelViewModel
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.signin.SignInModule
import com.github.travelplannerapp.signin.SignInViewModel
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.traveldetails.TravelDetailsModule
import com.github.travelplannerapp.traveldetails.TravelDetailsViewModel
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

    @ContributesAndroidInjector(modules = [AddTravelViewModel::class, AddTravelModule::class])
    internal abstract fun bindAddTravelActivity(): AddTravelActivity

    @ContributesAndroidInjector(modules = [TravelDetailsViewModel::class, TravelDetailsModule::class])
    internal abstract fun bindTravelDetailsActivity(): TravelDetailsActivity

    // Add bindings for other sub-components here
}
