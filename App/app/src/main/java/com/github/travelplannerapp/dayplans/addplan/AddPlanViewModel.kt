package com.github.travelplannerapp.dayplans.addplan

import dagger.Binds
import dagger.Module

@Module
abstract class AddPlanViewModel {
    @Binds
    internal abstract fun provideAddPlanView(addPlanActivity: AddPlanActivity): AddPlanContract.View
}
