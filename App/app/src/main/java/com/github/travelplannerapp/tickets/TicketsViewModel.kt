package com.github.travelplannerapp.tickets

import dagger.Binds
import dagger.Module

@Module
abstract class TicketsViewModel {
    @Binds
    internal abstract fun provideTicketsView(ticketsActivity: TicketsActivity): TicketsContract.View
}