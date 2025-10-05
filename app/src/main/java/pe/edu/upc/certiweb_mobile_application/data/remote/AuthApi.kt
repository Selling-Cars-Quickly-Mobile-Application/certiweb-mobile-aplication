package pe.edu.upc.certiweb_mobile_application.data.remote

import pe.edu.upc.certiweb_mobile_application.data.model.LoginRequest
import pe.edu.upc.certiweb_mobile_application.data.model.RegisterRequest
import pe.edu.upc.certiweb_mobile_application.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    // json-server: login via query to users
    @GET("/users")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<List<User>>

    // register: post to users
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/users")
    suspend fun register(@Body request: RegisterRequest): Response<User>
}