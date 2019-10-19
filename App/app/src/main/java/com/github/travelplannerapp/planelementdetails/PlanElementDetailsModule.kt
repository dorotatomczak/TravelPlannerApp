package com.github.travelplannerapp.planelementdetails

import dagger.Module
import dagger.Provides

@Module
class PlanElementDetailsModule {

    @Provides
    internal fun providePlanElementDetailsPresenter(planElementDetailsView: PlanElementDetailsContract.View): PlanElementDetailsContract.Presenter {
        return PlanElementDetailsPresenter(planElementDetailsView)
    }
}
