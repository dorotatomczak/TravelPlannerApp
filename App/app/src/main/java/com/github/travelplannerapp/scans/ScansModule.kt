package com.github.travelplannerapp.scans

import dagger.Module
import dagger.Provides

/**
 * Define ScansActivity-specific dependencies here.
 */
@Module
class ScansModule {

    @Provides
    internal fun provideScansPresenter(scansActivity: ScansActivity, scansView: ScansContract.View): ScansContract.Presenter {
        val travelId = scansActivity.intent.getIntExtra(ScansActivity.EXTRA_TRAVEL_ID, -1)
        return ScansPresenter(scansView, travelId)
    }
}
