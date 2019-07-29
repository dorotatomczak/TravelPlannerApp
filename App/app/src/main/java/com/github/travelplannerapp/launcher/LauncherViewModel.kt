package com.github.travelplannerapp.launcher

import dagger.Binds
import dagger.Module

@Module
abstract class LauncherViewModel {
    @Binds
    internal abstract fun provideLauncherView(launcherActivity: LauncherActivity): LauncherContract.View
}
