package com.github.travelplannerapp.traveldetails.addtransport

import com.github.travelplannerapp.communication.commonmodel.Place
import dagger.Module
import dagger.Provides

@Module
class AddTransportModule {

    @Provides
    internal fun provideAddTransportPresenter(addTransportActivity: AddTransportActivity, addTransportView: AddTransportContract.View): AddTransportContract.Presenter {
        val from = addTransportActivity.intent.getSerializableExtra(AddTransportActivity.EXTRA_FROM) as Place
        val to = addTransportActivity.intent.getSerializableExtra(AddTransportActivity.EXTRA_TO) as Place
        val travelId = addTransportActivity.intent.getIntExtra(AddTransportActivity.EXTRA_TRAVEL_ID, -1)

        return AddTransportPresenter(from, to, travelId, addTransportView)
    }
}
