package com.github.travelplannerapp.planelementdetails

import dagger.Binds
import dagger.Module

@Module
abstract class PlanElementDetailsViewModel {
    @Binds
    internal abstract fun providePlanElementDetailsView(planElementDetailsActivity: PlanElementDetailsActivity): PlanElementDetailsContract.View
}