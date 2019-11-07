package com.github.travelplannerapp.planelementdetails

import com.github.travelplannerapp.communication.commonmodel.PlanElement
import dagger.Module
import dagger.Provides

@Module
class PlanElementDetailsModule {

    @Provides
    internal fun providePlanElementDetailsPresenter(planElementDetailsActivity: PlanElementDetailsActivity,
                                                    planElementDetailsView: PlanElementDetailsContract.View): PlanElementDetailsContract.Presenter {

        val planElement = planElementDetailsActivity.intent.getSerializableExtra(PlanElementDetailsActivity.EXTRA_PLAN_ELEMENT) as PlanElement
        val placeId = planElement.placeId
        val placeName = planElementDetailsActivity.intent.getStringExtra(PlanElementDetailsActivity.EXTRA_PLACE_NAME)
        val travelId = planElementDetailsActivity.intent.getIntExtra(PlanElementDetailsActivity.EXTRA_TRAVEL_ID, -1)
        val placeHref = planElement.place.href
        val placeAverageRating = planElement.place.averageRating

        return PlanElementDetailsPresenter(planElement, placeId, placeName!!, placeHref, placeAverageRating!!, travelId, planElementDetailsView)
    }
}
