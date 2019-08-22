package com.github.travelplannerapp.communication

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


object CommunicationService {
    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
    //remote server: https://journello.herokuapp.com/

    private const val serverUrl: String = "http://10.0.2.2:8080/"

    val serverApi = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ServerApi::class.java)
}

interface ServerApi {

    @GET("/travels")
    fun getTravels(@Query("userId") userId: Int, @Query("auth") auth: String): Observable<List<String>>

    @POST("/authenticate")
    fun authenticate(@Body jsonString: String): Observable<String>

    @POST("/register")
    fun register(@Body jsonString: String): Observable<String>

    @POST("/addtravel")
    fun addTravel(@Body jsonString: String): Observable<String>
}
