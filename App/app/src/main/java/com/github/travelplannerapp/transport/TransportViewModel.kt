package com.github.travelplannerapp.transport

import dagger.Binds
import dagger.Module

@Module
abstract class TransportViewModel {
    @Binds
    internal abstract fun provideTransportView(transportActivity: TransportActivity): TransportContract.View
}