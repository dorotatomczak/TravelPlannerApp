package com.github.travelplannerapp.di

import com.github.travelplannerapp.accommodation.AccommodationActivity
import com.github.travelplannerapp.accommodation.AccommodationModule
import com.github.travelplannerapp.accommodation.AccommodationViewModel
import com.github.travelplannerapp.dayplans.addplan.AddPlanActivity
import com.github.travelplannerapp.dayplans.addplan.AddPlanModule
import com.github.travelplannerapp.dayplans.addplan.AddPlanViewModel
import com.github.travelplannerapp.dayplans.DayPlansActivity
import com.github.travelplannerapp.dayplans.DayPlansModule
import com.github.travelplannerapp.dayplans.DayPlansViewModel
import com.github.travelplannerapp.initializers.InitializerModule
import com.github.travelplannerapp.launcher.LauncherActivity
import com.github.travelplannerapp.launcher.LauncherModule
import com.github.travelplannerapp.launcher.LauncherViewModel
import com.github.travelplannerapp.scanner.ScannerActivity
import com.github.travelplannerapp.scanner.ScannerModule
import com.github.travelplannerapp.scanner.ScannerViewModel
import com.github.travelplannerapp.dayplans.searchelement.SearchElementActivity
import com.github.travelplannerapp.dayplans.searchelement.SearchElementModule
import com.github.travelplannerapp.dayplans.searchelement.SearchElementViewModel
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.signin.SignInModule
import com.github.travelplannerapp.signin.SignInViewModel
import com.github.travelplannerapp.signup.SignUpActivity
import com.github.travelplannerapp.signup.SignUpModule
import com.github.travelplannerapp.signup.SignUpViewModel
import com.github.travelplannerapp.tickets.TicketsActivity
import com.github.travelplannerapp.tickets.TicketsModule
import com.github.travelplannerapp.tickets.TicketsViewModel
import com.github.travelplannerapp.transport.TransportActivity
import com.github.travelplannerapp.transport.TransportModule
import com.github.travelplannerapp.transport.TransportViewModel
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

    @ContributesAndroidInjector(modules = [AccommodationViewModel::class, AccommodationModule::class])
    internal abstract fun bindAccommodationActivity(): AccommodationActivity

    @ContributesAndroidInjector(modules = [AddPlanViewModel::class, AddPlanModule::class])
    internal abstract fun bindAddPlanActivity(): AddPlanActivity

    @ContributesAndroidInjector(modules = [DayPlansViewModel::class, DayPlansModule::class])
    internal abstract fun bindDayPlansActivity(): DayPlansActivity

    @ContributesAndroidInjector(modules = [LauncherViewModel::class, LauncherModule::class, InitializerModule::class])
    internal abstract fun bindLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector(modules = [SearchElementViewModel::class, SearchElementModule::class])
    internal abstract fun bindSearchElementActivity(): SearchElementActivity

    @ContributesAndroidInjector(modules = [SignInViewModel::class, SignInModule::class])
    internal abstract fun bindSignInActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [SignUpViewModel::class, SignUpModule::class])
    internal abstract fun bindSignUpActivity(): SignUpActivity


    @ContributesAndroidInjector(modules = [ScannerViewModel::class, ScannerModule::class])
    internal abstract fun bindScannerActivity(): ScannerActivity

    @ContributesAndroidInjector(modules = [TicketsViewModel::class, TicketsModule::class])
    internal abstract fun bindTicketsActivity(): TicketsActivity

    @ContributesAndroidInjector(modules = [TransportViewModel::class, TransportModule::class])
    internal abstract fun bindTransportActivity(): TransportActivity

    @ContributesAndroidInjector(modules = [TravelDetailsViewModel::class, TravelDetailsModule::class])
    internal abstract fun bindTravelDetailsActivity(): TravelDetailsActivity

    @ContributesAndroidInjector(modules = [TravelsViewModel::class, TravelsModule::class])
    internal abstract fun bindTravelsActivity(): TravelsActivity

    // Add bindings for other sub-components here
}