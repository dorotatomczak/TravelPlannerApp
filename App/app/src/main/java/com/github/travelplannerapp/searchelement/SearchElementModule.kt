package com.github.travelplannerapp.searchelement

import dagger.Module
import dagger.Provides

/**
 * Define SearchElementActivity-specific dependencies here.
 */
@Module
class SearchElementModule {

    @Provides
    internal fun provideDayElementPresenter(DayElementView: SearchElementContract.View): SearchElementContract.Presenter {
        return SearchElementPresenter(DayElementView)
    }
}