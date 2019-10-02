package com.github.travelplannerapp.launcher

import dagger.Module
import dagger.Provides

/**
 * Define LauncherActivity-specific dependencies here.
 */
@Module
class LauncherModule {

    @Provides
    internal fun provideLauncherPresenter(launcherView: LauncherContract.View): LauncherContract.Presenter {
        return LauncherPresenter(launcherView)
    }
}