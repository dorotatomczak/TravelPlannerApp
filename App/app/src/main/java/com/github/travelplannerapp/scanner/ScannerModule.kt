package com.github.travelplannerapp.scanner

import dagger.Module
import dagger.Provides

/**
 * Define ScannerActivity-specific dependencies here.
 */
@Module
class ScannerModule {

    @Provides
    internal fun provideScannerPresenter(scannerView: ScannerContract.View): ScannerContract.Presenter {
        return ScannerPresenter(scannerView)
    }
}