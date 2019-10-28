package com.github.travelplannerapp.planelementdetails

import dagger.Module
import dagger.Provides

@Module
class PlanElementDetailsModule {

    @Provides
    internal fun providePlanElementDetailsPresenter(planElementDetailsActivity: PlanElementDetailsActivity,
                                                    planElementDetailsView: PlanElementDetailsContract.View): PlanElementDetailsContract.Presenter {

        val placeId = planElementDetailsActivity.intent.getIntExtra(PlanElementDetailsActivity.EXTRA_PLACE_ID, -1)
        val placeName = planElementDetailsActivity.intent.getStringExtra(PlanElementDetailsActivity.EXTRA_PLACE_NAME)
        val placeHref = planElementDetailsActivity.intent.getStringExtra(PlanElementDetailsActivity.EXTRA_PLACE_HREF)
        val placeAverageRating = planElementDetailsActivity.intent.getStringExtra(PlanElementDetailsActivity.EXTRA_AVERAGE_RATING)

        return PlanElementDetailsPresenter(placeId, placeName!!, placeHref!!, placeAverageRating!!, planElementDetailsView)
    }
}
