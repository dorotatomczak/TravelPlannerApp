package com.github.travelplannerapp.travels

import dagger.Module
import dagger.Provides

/**
 * Define TravelsActivity-specific dependencies here.
 */
@Module
class TravelsModule {

    @Provides
    internal fun provideTravelsPresenter(travelsView: TravelsContract.View): TravelsContract.Presenter {
        return TravelsPresenter(travelsView)
    }
}