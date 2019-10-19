package com.github.travelplannerapp.traveldetails.searchelement

import dagger.Binds
import dagger.Module

@Module
abstract class SearchElementViewModel {
    @Binds
    internal abstract fun provideDayElementView(DayElementActivity: SearchElementActivity): SearchElementContract.View
}
