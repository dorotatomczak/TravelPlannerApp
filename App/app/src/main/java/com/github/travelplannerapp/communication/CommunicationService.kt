package com.github.travelplannerapp.communication

import com.github.travelplannerapp.communication.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody


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

    @FormUrlEncoded
    @POST("/authorize")
    fun authorize(@Header("authorization") token: String, @Field("userId") userId: Int): Single<Response<Void>>

    @POST("/authenticate")
    fun authenticate(@Body body: SignInRequest): Single<Response<SignInResponse>>

    @POST("/register")
    fun register(@Body body: SignUpRequest): Single<Response<Void>>

    @GET("/travels")
    fun getTravels(@Header("authorization") token: String, @Query("userId") userId: Int): Observable<Response<List<Travel>>>

    @POST("/addtravel")
    fun addTravel(@Header("authorization") token: String, @Body body: AddTravelRequest): Single<Response<Travel>>

    @Multipart
    @POST("/uploadScan")
    fun uploadScan(@Header("Authorization") auth: String, @Part("userId") userId: RequestBody,
                   @Part("travelId") travelId: RequestBody, @Part file: MultipartBody.Part): Single<Response<Void>>
}
