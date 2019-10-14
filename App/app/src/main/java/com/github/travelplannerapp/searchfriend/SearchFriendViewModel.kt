package com.github.travelplannerapp.searchfriend

import dagger.Binds
import dagger.Module

@Module
abstract class SearchFriendViewModel {
    @Binds
    internal abstract fun provideSearchFriendView(UserElementActivity: SearchFriendActivity): SearchFriendContract.View
}
