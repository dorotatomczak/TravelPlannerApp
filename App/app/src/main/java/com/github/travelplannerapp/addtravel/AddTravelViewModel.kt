package com.github.travelplannerapp.addtravel

import dagger.Binds
import dagger.Module

@Module
abstract class AddTravelViewModel {
    @Binds
    internal abstract fun provideAddTravelView(addTravelActivity: AddTravelActivity): AddTravelContract.View
}