package pe.edu.upc.certiweb_mobile_application.data

import pe.edu.upc.certiweb_mobile_application.data.model.User
import pe.edu.upc.certiweb_mobile_application.data.remote.ApiClient
import pe.edu.upc.certiweb_mobile_application.data.remote.UserApi

class UserRepository(private val api: UserApi = ApiClient.userApi) {
    suspend fun fetchByIdOrEmail(id: String?, email: String?): Result<User> {
        return try {
            if (!id.isNullOrBlank()) {
                val res = api.getById(id)
                if (res.isSuccessful) {
                    res.body()?.let { return Result.success(it) }
                }
            }
            if (!email.isNullOrBlank()) {
                val res2 = api.getByEmail(email)
                if (res2.isSuccessful) {
                    val user = res2.body().orEmpty().firstOrNull()
                    if (user != null) return Result.success(user)
                }
            }
            Result.failure(Exception("No se pudo obtener el usuario"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}