package com.github.travelplannerapp.communication

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


object CommunicationService {
    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
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
    fun getTravels(@Query("email") name: String, @Query("auth") auth: String): Observable<List<String>>

    @POST("/authenticate")
    fun authenticate(@Body jsonString: String): Observable<String>

    @POST("/register")
    fun register(@Body jsonString: String): Observable<String>

    @Multipart
    @POST("/uploadScan")
    fun uploadScan(@Header("Authorization") auth: String, @Part("email") email: RequestBody, @Part file: MultipartBody.Part): Observable<String>
}
