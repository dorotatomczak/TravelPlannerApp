package com.github.travelplannerapp.traveldetails

import dagger.Module
import dagger.Provides

@Module
class TravelDetailsModule {
    @Provides
    internal fun provideTravelsPresenter(travelDetailsActivity: TravelDetailsActivity, travelDetailsView: TravelDetailsContract.View): TravelDetailsContract.Presenter {
        val travelId = travelDetailsActivity.intent.getIntExtra(TravelDetailsActivity.EXTRA_TRAVEL_ID, -1)
        return TravelDetailsPresenter(travelId, travelDetailsView)
    }
}