package com.github.travelplannerapp.dayplans.addplan

import dagger.Module
import dagger.Provides

@Module
class AddPlanModule {

    @Provides
    internal fun providAddPlanPresenter(addPlanView: AddPlanContract.View): AddPlanContract.Presenter {
        return AddPlanPresenter(addPlanView)
    }
}
