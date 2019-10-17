package com.github.travelplannerapp.traveldetails.addplanelement

import dagger.Module
import dagger.Provides

@Module
class AddPlanElementModule {

    @Provides
    internal fun provideAddPlanPresenter(addPlanElementActivity: AddPlanElementActivity, addPlanElementView: AddPlanElementContract.View): AddPlanElementContract.Presenter {
        val travelId = addPlanElementActivity.intent.getIntExtra(AddPlanElementActivity.EXTRA_TRAVEL_ID, -1)
        return AddPlanElementPresenter(travelId, addPlanElementView)
    }
}
