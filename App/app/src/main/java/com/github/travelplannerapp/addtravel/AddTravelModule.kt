package com.github.travelplannerapp.addtravel

import dagger.Module
import dagger.Provides

/**
 * Define AddTravelActivity-specific dependencies here.
 */
@Module
class AddTravelModule {

    @Provides
    internal fun provideAddTravelPresenter(addTravelView: AddTravelContract.View): AddTravelContract.Presenter {
        return AddTravelPresenter(addTravelView)
    }
}