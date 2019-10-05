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

    private const val serverUrl: String = "http://192.168.1.8:8080/"
    val serverApi: ServerApi = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(AuthTokenInterceptor()).build())
            .build()
            .create(ServerApi::class.java)

    fun getScanUrl(name: String, userId: Int): String {
        return "$serverUrl/users/$userId/scans/$name"
    }
}

interface ServerApi {

    @POST("/user-management/authorize")
    fun authorize(): Single<Response<Unit>>

    @POST("/user-management/authenticate")
    fun authenticate(@Body body: SignInRequest): Single<Response<SignInResponse>>

    @POST("/user-management/register")
    fun register(@Body body: SignUpRequest): Single<Response<Unit>>

    @GET("/users/{userId}/travels")
    fun getTravels(@Path("userId") userId: Int): Observable<Response<List<Travel>>>

    @POST("/users/{userId}/travels")
    fun addTravel(@Path("userId") userId: Int, @Body travelName: String): Single<Response<Travel>>

    @PUT("/users/{userId}/travels")
    fun changeTravelName(@Path("userId") userId: Int, @Body travel: Travel): Single<Response<Travel>>

    @HTTP(method = "DELETE", path = "/users/{userId}/travels", hasBody = true)
    fun deleteTravels(@Path("userId") userId: Int, @Body travelIds: MutableSet<Int>): Single<Response<Unit>>

    @Multipart
    @POST("/users/{userId}/scans")
    fun uploadScan(@Path("userId") userId: Int, @Part("travelId") travelId: RequestBody, @Part file: MultipartBody.Part): Single<Response<Scan>>

    @HTTP(method = "DELETE", path = "/users/{userId}/scans", hasBody = true)
    fun deleteScans(@Path("userId") userId: Int, @Body scans: MutableSet<Scan>): Single<Response<Unit>>

    @GET("/users/{userId}/scans")
    fun getScans(@Path("userId") userId: Int, @Query("travelId") travelId: Int): Single<Response<List<Scan>>>

    @GET("/here-management/cities")
    fun findCities(@Query("query") query: String): Single<Response<List<CityObject>>>

    @GET("/here-management/objects")
    fun findObjects(@Query("cat") category: String, @Query("west") west: String, @Query("north") north: String,
                    @Query("east") east: String, @Query("south") south: String): Single<Response<Array<Place>>>

    @GET("/here-management/objects/{objectId}/contacts")
    fun getContacts(@Path("objectId") objectId: String, @Query("query") query: String): Single<Response<Contacts>>

    @GET("users/{userId}/friends")
    fun getFriends(@Path("userId") userId: Int): Observable<Response<List<String>>>

    @POST("/users/{userId}/friends")
    fun addFriend(@Path("userId") userId: Int, @Body friendEmail: String): Single<Response<Boolean>>

    @GET("/user-management/usersemails")
    fun findUsersEmails(@Query("query") query: String): Single<Response<MutableList<UserInfo>>>

    @POST("/deletefriend")
    fun deleteFriend(@Body friendEmail: String): Single<Response<Boolean>>
}
