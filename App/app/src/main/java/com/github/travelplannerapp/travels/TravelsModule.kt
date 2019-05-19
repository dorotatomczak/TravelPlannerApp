package com.github.travelplannerapp.travels

import dagger.Module
import dagger.Provides

/**
 * Define LoginActivity-specific dependencies here.
 */
@Module
class TravelsModule {

    @Provides
    internal fun provideLobbyPresenter(travelsView: TravelsContract.View): TravelsPresenter {
        return TravelsPresenter(travelsView)
    }
}