package com.github.travelplannerapp.dayplans

import dagger.Module
import dagger.Provides

/**
 * Define DayPlansActivity-specific dependencies here.
 */
@Module
class DayPlansModule {

    @Provides
    internal fun provideDayPlansPresenter(dayPlansView: DayPlansContract.View): DayPlansContract.Presenter {
        return DayPlansPresenter(dayPlansView)
    }
}