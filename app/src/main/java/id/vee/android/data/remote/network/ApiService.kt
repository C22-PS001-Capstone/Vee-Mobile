package id.vee.android.data.remote.network

import id.vee.android.data.remote.response.BasicResponse
import id.vee.android.data.remote.response.LoginResponse
import id.vee.android.data.remote.response.UserDetailResponse
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
        @Field("price") expense: Int
    ): BasicResponse

    @FormUrlEncoded
    @PUT("/authentications")
    suspend fun refreshToken(
        @Field("refreshToken") refreshToken: String
    ): LoginResponse
}