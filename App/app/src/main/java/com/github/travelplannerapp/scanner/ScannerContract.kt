package com.github.travelplannerapp.scanner

interface ScannerContract {

    interface View {
        fun closeScanner()
    }

    interface Presenter {
        fun takeScan()
    }

}