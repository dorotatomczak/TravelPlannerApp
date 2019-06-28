package com.github.travelplannerapp.tickets

import dagger.Module
import dagger.Provides

/**
 * Define TicketsActivity-specific dependencies here.
 */
@Module
class TicketsModule {

    @Provides
    internal fun provideTicketsPresenter(ticketsView: TicketsContract.View): TicketsContract.Presenter {
        return TicketsPresenter(ticketsView)
    }
}