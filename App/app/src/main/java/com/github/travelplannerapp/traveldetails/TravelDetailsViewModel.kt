package com.github.travelplannerapp.traveldetails

import dagger.Binds
import dagger.Module

@Module
abstract class TravelDetailsViewModel {
    @Binds
    internal abstract fun provideTravelDetailsView(travelDetailsActivity: TravelDetailsActivity): TravelDetailsContract.View
}