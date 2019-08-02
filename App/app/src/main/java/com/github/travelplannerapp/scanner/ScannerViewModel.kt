package com.github.travelplannerapp.scanner

import dagger.Binds
import dagger.Module

@Module
abstract class ScannerViewModel {
    @Binds
    internal abstract fun provideScannerView(scannerActivity: ScannerActivity): ScannerContract.View
}