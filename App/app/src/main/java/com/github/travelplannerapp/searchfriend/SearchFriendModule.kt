package com.github.travelplannerapp.searchfriend

import dagger.Module
import dagger.Provides

@Module
class SearchFriendModule {

    @Provides
    internal fun provideSearchFriendPresenter(FriendElementView: SearchFriendContract.View): SearchFriendContract.Presenter {
        return SearchFriendPresenter(FriendElementView)
    }
}
