package com.github.travelplannerapp.tickets

import dagger.Module
import dagger.Provides

/**
 * Define TicketsActivity-specific dependencies here.
 */
@Module
class TicketsModule {

    @Provides
    internal fun provideTicketsPresenter(ticketsActivity: TicketsActivity, ticketsView: TicketsContract.View): TicketsContract.Presenter {
        val travelId = ticketsActivity.intent.getIntExtra(TicketsActivity.EXTRA_TRAVEL_ID, -1)
        return TicketsPresenter(ticketsView, travelId)
    }
}