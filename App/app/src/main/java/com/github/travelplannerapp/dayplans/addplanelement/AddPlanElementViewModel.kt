package com.github.travelplannerapp.dayplans.addplanelement

import dagger.Binds
import dagger.Module

@Module
abstract class AddPlanElementViewModel {
    @Binds
    internal abstract fun provideAddPlanView(addPlanElementActivity: AddPlanElementActivity): AddPlanElementContract.View
}
