package com.github.travelplannerapp.dayplans

import dagger.Binds
import dagger.Module

@Module
abstract class DayPlansViewModel {
    @Binds
    internal abstract fun provideDayPlansView(dayPlansActivity: DayPlansActivity): DayPlansContract.View
}