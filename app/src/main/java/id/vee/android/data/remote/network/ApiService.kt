package id.vee.android.data.remote.network

import id.vee.android.data.remote.response.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/users")
    suspend fun signup(
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String
    ): BasicResponse

    @FormUrlEncoded
    @PUT("/users")
    suspend fun updateName(
        @Header("Authorization") token: String,
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String
    ): BasicResponse

    @FormUrlEncoded
    @PUT("/passwords")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Field("passwordCurrent") passwordCurrent: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String
    ): BasicResponse

    @FormUrlEncoded
    @POST("/authentications")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("/users")
    suspend fun userDetail(
        @Header("Authorization") token: String
    ): UserDetailResponse

    @FormUrlEncoded
    @POST("/activities")
    suspend fun insertActivity(
        @Header("Authorization") token: String,
        @Field("date") date: String,
        @Field("km") distance: Int,
        @Field("liter") litre: Int,
        @Field("price") expense: Int,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double
    ): BasicResponse

    @FormUrlEncoded
    @PUT("/authentications")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): LoginResponse

    @DELETE("/authentications")
    suspend fun logout(
        @Query("refreshToken") refreshToken: String
    ): BasicResponse

    @GET("/activities")
    suspend fun getActivity(
        @Header("Authorization") token: String,
    ): ActivityListResponse

    @DELETE("/activities/{id}")
    fun deleteActivity(
        @Path("id") id: String,
        @Header("Authorization") bearer: String
    ): BasicResponse

    @FormUrlEncoded
    @PUT("/activities/{id}")
    suspend fun updateActivity(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Field("date") date: String,
        @Field("km") distance: Int,
        @Field("liter") litre: Int,
        @Field("price") expense: Int,
        @Field("lat") lat: Double,
        @Field("lon") lon: Double
    ): BasicResponse

    @GET("/gasstations")
    suspend fun getGasStations(
        @Field("lat") lat: Double,
        @Field("lon") lon: Double
    ): GasStationsResponse
}