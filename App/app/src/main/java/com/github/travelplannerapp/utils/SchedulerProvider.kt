package com.github.travelplannerapp.utils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


object SchedulerProvider {

    fun computation(): Scheduler {
        return Schedulers.computation()
    }

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}
