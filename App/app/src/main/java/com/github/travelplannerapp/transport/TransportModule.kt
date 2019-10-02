package com.github.travelplannerapp.transport

import dagger.Module
import dagger.Provides

/**
 * Define TicketsActivity-specific dependencies here.
 */
@Module
class TransportModule {

    @Provides
    internal fun provideTransportPresenter(transportView: TransportContract.View): TransportContract.Presenter {
        return TransportPresenter(transportView)
    }
}