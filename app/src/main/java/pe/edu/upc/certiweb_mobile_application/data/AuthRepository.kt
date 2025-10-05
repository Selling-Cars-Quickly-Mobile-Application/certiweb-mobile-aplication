package pe.edu.upc.certiweb_mobile_application.data

import pe.edu.upc.certiweb_mobile_application.data.model.RegisterRequest
import pe.edu.upc.certiweb_mobile_application.data.model.User
import pe.edu.upc.certiweb_mobile_application.data.remote.ApiClient

class AuthRepository {
    private val api = ApiClient.authApi

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val res = api.login(email, password)
            if (res.isSuccessful) {
                val users = res.body().orEmpty()
                val user = users.firstOrNull()
                if (user != null) Result.success(user) else Result.failure(Exception("Invalid credentials"))
            } else Result.failure(Exception("Login failed: ${res.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(request: RegisterRequest): Result<User> {
        return try {
            val res = api.register(request)
            if (res.isSuccessful) {
                val user = res.body()
                if (user != null) Result.success(user) else Result.failure(Exception("Register failed"))
            } else Result.failure(Exception("Register failed: ${res.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}