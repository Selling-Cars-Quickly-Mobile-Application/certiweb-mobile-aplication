package pe.edu.upc.certiweb_mobile_application.data.remote

import pe.edu.upc.certiweb_mobile_application.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("/users/{id}")
    suspend fun getById(@Path("id") id: String): Response<User>

    @GET("/users")
    suspend fun getByEmail(@Query("email") email: String): Response<List<User>>
}