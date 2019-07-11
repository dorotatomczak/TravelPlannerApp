package com.github.travelplannerapp.communication

import com.github.travelplannerapp.travels.TravelsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@Singleton
class CommunicationService {
    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
    private var serverUrl: String = "http://10.0.2.2:8080"

    fun getUrl() : String{
        return serverUrl
    }
}