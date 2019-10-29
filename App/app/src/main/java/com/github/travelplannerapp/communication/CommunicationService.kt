package com.github.travelplannerapp.communication

import com.github.travelplannerapp.communication.appmodel.CityObject
import com.github.travelplannerapp.communication.appmodel.Scan
import com.github.travelplannerapp.communication.appmodel.Travel
import com.github.travelplannerapp.communication.commonmodel.*
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

    private const val serverUrl: String = "http://10.0.2.2:8080/"
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

    fun getTravelImageUrl(name: String, userId: Int): String {
        return "$serverUrl/users/$userId/travels/$name"
    }
}

interface ServerApi {

    // user-management
    @POST("user-management/authorize")
    fun authorize(): Single<Response<Unit>>

    @POST("user-management/authenticate")
    fun authenticate(@Body body: SignInRequest): Single<Response<SignInResponse>>

    @POST("user-management/register")
    fun register(@Body body: SignUpRequest): Single<Response<Unit>>

    @GET("user-management/usersemails")
    fun findMatchingEmails(@Query("query") query: String): Single<Response<MutableList<UserInfo>>>

    // users - travels
    @GET("users/{userId}/travels")
    fun getTravels(@Path("userId") userId: Int): Single<Response<List<Travel>>>

    @POST("users/{userId}/travels")
    fun addTravel(@Path("userId") userId: Int, @Body travelName: String): Single<Response<Travel>>

    @PUT("users/{userId}/travels")
    fun changeTravelName(@Path("userId") userId: Int, @Body travel: Travel): Single<Response<Travel>>

    @Multipart
    @PUT("users/{userId}/travels")
    fun uploadTravelImage(@Path("userId") userId: Int, @Part("travelId") travelId: RequestBody, @Part file: MultipartBody.Part): Single<Response<Travel>>

    @HTTP(method = "DELETE", path = "users/{userId}/travels", hasBody = true)
    fun deleteTravels(@Path("userId") userId: Int, @Body travelIds: MutableSet<Int>): Single<Response<Unit>>

    //users - scans
    @Multipart
    @POST("users/{userId}/scans")
    fun uploadScan(@Path("userId") userId: Int, @Part("travelId") travelId: RequestBody, @Part file: MultipartBody.Part): Single<Response<Scan>>

    @HTTP(method = "DELETE", path = "users/{userId}/scans", hasBody = true)
    fun deleteScans(@Path("userId") userId: Int, @Body scans: MutableSet<Scan>): Single<Response<Unit>>

    @GET("users/{userId}/scans")
    fun getScans(@Path("userId") userId: Int, @Query("travelId") travelId: Int): Single<Response<List<Scan>>>

    //users - friends
    @GET("users/{userId}/friends")
    fun getFriends(@Path("userId") userId: Int): Single<Response<List<UserInfo>>>

    @HTTP(method = "DELETE", path = "users/{userId}/friends", hasBody = false)
    fun deleteFriends(@Path("userId") userId: Int, @Query("friendsIds") friendsIds: MutableSet<Int>): Single<Response<Unit>>

    @POST("users/{userId}/friends")
    fun addFriend(@Path("userId") userId: Int, @Body friend: UserInfo): Single<Response<UserInfo>>

    //users - plans
    @GET("users/{userId}/travels/{travelId}/plans")
    fun getPlanElements(@Path("userId") userId: Int, @Path("travelId") travelId: Int): Single<Response<List<PlanElement>>>

    @POST("users/{userId}/travels/{travelId}/plans")
    fun addPlanElement(@Path("userId") userId: Int, @Path("travelId") travelId: Int, @Body planElement: PlanElement): Single<Response<PlanElement>>

    @HTTP(method = "DELETE", path = "users/{userId}/plans", hasBody = true)
    fun deletePlanElements(@Path("userId") userId: Int, @Body planElementIds: List<Int>): Single<Response<Unit>>

    @PUT("/users/{userId}/travels/{travelId}/plans")
    fun updatePlanElement(@Path("userId") userId: Int, @Path("travelId") travelId: Int, @Body planElement: PlanElement): Single<Response<Unit>>

    //here and google
    @GET("here-management/cities")
    fun findCities(@Query("query") query: String): Single<Response<List<CityObject>>>

    @GET("here-management/objects")
    fun findObjects(@Query("cat") category: String, @Query("west") west: String, @Query("north") north: String,
                    @Query("east") east: String, @Query("south") south: String): Single<Response<Array<Place>>>

    @GET("here-management/objects/{objectId}/contacts")
    fun getContacts(@Path("objectId") objectId: String, @Query("query") query: String): Single<Response<Contacts>>

    @GET("here-management/objects/{objectId}")
    fun getPlace(@Path("objectId") objectId: String, @Query("query") query: String): Single<Response<PlaceData>>

    @GET("google-management/routes")
    fun getTransport(@Query("origin_latitude") originLat: String,
                     @Query("origin_longitude") originLng: String,
                     @Query("destination_latitude") destinationLat: String,
                     @Query("destination_longitude") destinationLng: String,
                     @Query("travel_mode") travelMode: String,
                     @Query("departure_time") departureTime: String): Single<Response<Routes>>

    //recommendation - places
    @POST("users/{userId}/places/{placeId}/rating")
    fun ratePlace(@Path("userId") userId: Int, @Path("placeId") placeId: Int, @Query("rating") rating: Int): Single<Response<Unit>>

    @GET("users/{userId}/places/{placeId}/rating")
    fun getPlaceRating(@Path("userId") userId: Int, @Path("placeId") placeId: Int): Single<Response<Int>>
}
