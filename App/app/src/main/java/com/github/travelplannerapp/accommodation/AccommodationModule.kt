package com.github.travelplannerapp.accommodation

import dagger.Module
import dagger.Provides

/**
 * Define AccommodationActivity-specific dependencies here.
 */
@Module
class AccommodationModule {

    @Provides
    internal fun provideAccommodationPresenter(accommodationView: AccommodationContract.View): AccommodationContract.Presenter {
        return AccommodationPresenter(accommodationView)
    }
}