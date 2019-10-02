package com.github.travelplannerapp.scanner

import dagger.Module
import dagger.Provides

/**
 * Define ScannerActivity-specific dependencies here.
 */
@Module
class ScannerModule {

    @Provides
    internal fun provideScannerPresenter(scannerActivity: ScannerActivity, scannerView: ScannerContract.View): ScannerContract.Presenter {
        val travelId = scannerActivity.intent.getIntExtra(ScannerActivity.EXTRA_TRAVEL_ID, -1)
        return ScannerPresenter(scannerView, travelId)
    }
}