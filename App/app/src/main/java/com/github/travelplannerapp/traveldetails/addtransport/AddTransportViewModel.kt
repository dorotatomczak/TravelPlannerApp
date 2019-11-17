package com.github.travelplannerapp.traveldetails.addtransport

import dagger.Binds
import dagger.Module

@Module
abstract class AddTransportViewModel {
    @Binds
    internal abstract fun provideTransportView(addTransportActivity: AddTransportActivity): AddTransportContract.View
}
