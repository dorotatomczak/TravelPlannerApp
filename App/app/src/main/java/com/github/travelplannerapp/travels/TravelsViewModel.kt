package com.github.travelplannerapp.travels

import dagger.Binds
import dagger.Module

@Module
abstract class TravelsViewModel {
    @Binds
    internal abstract fun provideTravelsView(travelsActivity: TravelsActivity): TravelsContract.View
}
