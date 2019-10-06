package com.github.travelplannerapp.dayplans.addplan

import dagger.Module
import dagger.Provides

@Module
class AddPlanModule {

    @Provides
    internal fun provideAddPlanPresenter(addPlanActivity: AddPlanActivity, addPlanView: AddPlanContract.View): AddPlanContract.Presenter {
        val travelId = addPlanActivity.intent.getIntExtra(AddPlanActivity.EXTRA_TRAVEL_ID, -1)
        return AddPlanPresenter(travelId, addPlanView)
    }
}
