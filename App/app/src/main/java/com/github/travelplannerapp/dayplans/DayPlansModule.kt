package com.github.travelplannerapp.dayplans

import dagger.Module
import dagger.Provides

/**
 * Define DayPlansActivity-specific dependencies here.
 */
@Module
class DayPlansModule {

    @Provides
    internal fun provideDayPlansPresenter(dayPlansActivity: DayPlansActivity, dayPlansView: DayPlansContract.View): DayPlansContract.Presenter {
        val travelId = dayPlansActivity.intent.getIntExtra(DayPlansActivity.EXTRA_TRAVEL_ID, -1)
        return DayPlansPresenter(travelId,dayPlansView)
    }
}
