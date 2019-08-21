package com.github.travelplannerapp.communication

import com.github.travelplannerapp.communication.model.AddTravelRequest
import com.github.travelplannerapp.communication.model.SignInRequest
import com.github.travelplannerapp.communication.model.SignInResponse
import com.github.travelplannerapp.communication.model.SignUpRequest
import com.github.travelplannerapp.communication.model.Travel
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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

    @POST("authorize")
    fun authorize(@Header("authorization") token: String, @Body userId: Int): Observable<Void>

    @POST("/authenticate")
    fun authenticate(@Body body: SignInRequest): Observable<SignInResponse>

    @POST("/register")
    fun register(@Body body: SignUpRequest): Observable<Void>

    @GET("/travels")
    fun getTravels(@Header("authorization") token: String, @Query("userId") userId: Int): Observable<List<Travel>>

    @POST("/addtravel")
    fun addTravel(@Header("authorization") token: String, @Body body: AddTravelRequest): Observable<Travel>

}
