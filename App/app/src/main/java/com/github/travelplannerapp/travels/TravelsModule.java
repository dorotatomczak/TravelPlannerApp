package com.github.travelplannerapp.travels;

import dagger.Module;
import dagger.Provides;

/**
 * Define LoginActivity-specific dependencies here.
 */
@Module
public class TravelsModule {

    @Provides
    TravelsPresenter provideLobbyPresenter(TravelsContract.View travelsView) {
        return new TravelsPresenter(travelsView);
    }
}