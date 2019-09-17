package com.github.travelplannerapp.communication

import com.github.travelplannerapp.communication.model.*
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*


object CommunicationService {
    //10.0.2.2 is "localhost" but on computer
    //localhost via emulator is emulator itself
    //remote server: https://journello.herokuapp.com/

    private const val serverUrl: String = "10.0.2.2:8080/"
    val serverApi: ServerApi = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(AuthTokenInterceptor()).build())
                .build()
                .create(ServerApi::class.java)

    fun getScanUrl(name: String): String {
        return "$serverUrl/scans/$name"
    }
}

interface ServerApi {

    @POST("/authorize")
    fun authorize(): Single<Response<Unit>>

    @POST("/authenticate")
    fun authenticate(@Body body: SignInRequest): Single<Response<SignInResponse>>

    @POST("/register")
    fun register(@Body body: SignUpRequest): Single<Response<Unit>>

    @GET("/travels")
    fun getTravels(): Observable<Response<List<Travel>>>

    @POST("/addtravel")
    fun addTravel(@Body travelName: String): Single<Response<Travel>>

    @POST("/deletetravels")
    fun deleteTravels(@Body travelIds: List<Int>): Single<Response<Unit>>

    @Multipart
    @POST("/uploadScan")
    fun uploadScan(@Part("travelId") travelId: RequestBody, @Part file: MultipartBody.Part): Single<Response<Scan>>

    @POST("/deleteScans")
    fun deleteScans(@Body scans: List<Scan>): Single<Response<Unit>>

    @GET("/scans")
    fun getScans(@Query("travelId") travelId: Int): Single<Response<List<Scan>>>
}
