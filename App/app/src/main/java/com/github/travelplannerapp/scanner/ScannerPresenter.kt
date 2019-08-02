package com.github.travelplannerapp.scanner

import com.github.travelplannerapp.BasePresenter

class ScannerPresenter(view: ScannerContract.View) : BasePresenter<ScannerContract.View>(view), ScannerContract.Presenter {

    override fun takeScan() {
        view.closeScanner()
    }

}