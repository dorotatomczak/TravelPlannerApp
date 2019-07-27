package com.github.travelplannerapp.scanner

interface ScannerContract {
    interface View
    interface Presenter {
        fun takeScan()
    }
}