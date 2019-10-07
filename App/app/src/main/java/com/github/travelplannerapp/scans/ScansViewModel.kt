package com.github.travelplannerapp.scans

import dagger.Binds
import dagger.Module

@Module
abstract class ScansViewModel {
    @Binds
    internal abstract fun provideScansView(scansActivity: ScansActivity): ScansContract.View
}
