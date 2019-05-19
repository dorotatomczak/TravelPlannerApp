package com.github.travelplannerapp.travels

import com.github.travelplannerapp.BasePresenter

class TravelsPresenter(view: TravelsContract.View) : BasePresenter<TravelsContract.View>(view), TravelsContract.Presenter
