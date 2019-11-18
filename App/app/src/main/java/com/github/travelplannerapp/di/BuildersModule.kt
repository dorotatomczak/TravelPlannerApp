package com.github.travelplannerapp.di

import com.github.travelplannerapp.initializers.InitializerModule
import com.github.travelplannerapp.launcher.LauncherActivity
import com.github.travelplannerapp.launcher.LauncherModule
import com.github.travelplannerapp.launcher.LauncherViewModel
import com.github.travelplannerapp.planelementdetails.PlanElementDetailsActivity
import com.github.travelplannerapp.planelementdetails.PlanElementDetailsModule
import com.github.travelplannerapp.planelementdetails.PlanElementDetailsViewModel
import com.github.travelplannerapp.scanner.ScannerActivity
import com.github.travelplannerapp.scanner.ScannerModule
import com.github.travelplannerapp.scanner.ScannerViewModel
import com.github.travelplannerapp.scans.ScansActivity
import com.github.travelplannerapp.scans.ScansModule
import com.github.travelplannerapp.scans.ScansViewModel
import com.github.travelplannerapp.searchfriend.SearchFriendActivity
import com.github.travelplannerapp.searchfriend.SearchFriendModule
import com.github.travelplannerapp.searchfriend.SearchFriendViewModel
import com.github.travelplannerapp.signin.SignInActivity
import com.github.travelplannerapp.signin.SignInModule
import com.github.travelplannerapp.signin.SignInViewModel
import com.github.travelplannerapp.signup.SignUpActivity
import com.github.travelplannerapp.signup.SignUpModule
import com.github.travelplannerapp.signup.SignUpViewModel
import com.github.travelplannerapp.traveldetails.TravelDetailsActivity
import com.github.travelplannerapp.traveldetails.TravelDetailsModule
import com.github.travelplannerapp.traveldetails.TravelDetailsViewModel
import com.github.travelplannerapp.traveldetails.addplanelement.AddPlanElementActivity
import com.github.travelplannerapp.traveldetails.addplanelement.AddPlanElementModule
import com.github.travelplannerapp.traveldetails.addplanelement.AddPlanElementViewModel
import com.github.travelplannerapp.traveldetails.addtransport.AddTransportActivity
import com.github.travelplannerapp.traveldetails.addtransport.AddTransportModule
import com.github.travelplannerapp.traveldetails.addtransport.AddTransportViewModel
import com.github.travelplannerapp.traveldetails.searchelement.SearchElementActivity
import com.github.travelplannerapp.traveldetails.searchelement.SearchElementModule
import com.github.travelplannerapp.traveldetails.searchelement.SearchElementViewModel
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

    @ContributesAndroidInjector(modules = [AddPlanElementViewModel::class, AddPlanElementModule::class])
    internal abstract fun bindAddPlanActivity(): AddPlanElementActivity

    @ContributesAndroidInjector(modules = [AddTransportViewModel::class, AddTransportModule::class])
    internal abstract fun bindTransportActivity(): AddTransportActivity

    @ContributesAndroidInjector(modules = [LauncherViewModel::class, LauncherModule::class, InitializerModule::class])
    internal abstract fun bindLauncherActivity(): LauncherActivity

    @ContributesAndroidInjector(modules = [PlanElementDetailsViewModel::class, PlanElementDetailsModule::class])
    internal abstract fun bindPlanElementDetailsActivity(): PlanElementDetailsActivity

    @ContributesAndroidInjector(modules = [SearchElementViewModel::class, SearchElementModule::class])
    internal abstract fun bindSearchElementActivity(): SearchElementActivity

    @ContributesAndroidInjector(modules = [SignInViewModel::class, SignInModule::class])
    internal abstract fun bindSignInActivity(): SignInActivity

    @ContributesAndroidInjector(modules = [SignUpViewModel::class, SignUpModule::class])
    internal abstract fun bindSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [ScannerViewModel::class, ScannerModule::class])
    internal abstract fun bindScannerActivity(): ScannerActivity

    @ContributesAndroidInjector(modules = [ScansViewModel::class, ScansModule::class])
    internal abstract fun bindScansActivity(): ScansActivity

    @ContributesAndroidInjector(modules = [TravelDetailsViewModel::class, TravelDetailsModule::class])
    internal abstract fun bindTravelDetailsActivity(): TravelDetailsActivity

    @ContributesAndroidInjector(modules = [TravelsViewModel::class, TravelsModule::class])
    internal abstract fun bindTravelsActivity(): TravelsActivity

    @ContributesAndroidInjector(modules = [SearchFriendViewModel::class, SearchFriendModule::class])
    internal abstract fun bindSearchFiendActivity(): SearchFriendActivity
}
