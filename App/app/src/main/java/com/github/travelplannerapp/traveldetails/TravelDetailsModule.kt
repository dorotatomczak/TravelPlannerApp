package com.github.travelplannerapp.traveldetails

import com.github.travelplannerapp.communication.appmodel.Travel
import dagger.Module
import dagger.Provides

@Module
class TravelDetailsModule {
    @Provides
    internal fun provideTravelsPresenter(travelDetailsActivity: TravelDetailsActivity, travelDetailsView: TravelDetailsContract.View): TravelDetailsContract.Presenter {
        val travel = travelDetailsActivity.intent.getSerializableExtra(TravelDetailsActivity.EXTRA_TRAVEL) as Travel
        return TravelDetailsPresenter(travel, travelDetailsView)
    }
}
