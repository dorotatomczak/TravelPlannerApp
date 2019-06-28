package com.github.travelplannerapp.accommodation

import dagger.Binds
import dagger.Module

@Module
abstract class AccommodationViewModel {
    @Binds
    internal abstract fun provideAccommodationView(accommodationActivity: AccommodationActivity): AccommodationContract.View
}