package pe.edu.upc.certiweb_mobile_application.public.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import pe.edu.upc.certiweb_mobile_application.config.DioClient
import pe.edu.upc.certiweb_mobile_application.environments.Environment
import java.time.Instant

class AuthService(private val context: Context) {
    private val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun login(email: String, password: String): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject()
                .put("email", email)
                .put("password", password)
                .toString()
                .toRequestBody(JSON)

            val request = Request.Builder()
                .url("${Environment.baseUrl}/auth/login")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val data = JSONObject(responseBody).toMap()
                        val token = data["token"]?.toString()
                        if (token != null && token.isNotEmpty()) {
                            prefs.edit().apply {
                                putString("authToken", token)
                                putString("currentUser", JSONObject(mapOf(
                                    "id" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "plan" to data["plan"]
                                )).toString())
                                putString("currentSession", JSONObject(mapOf(
                                    "userId" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "plan" to data["plan"],
                                    "isLoggedIn" to true,
                                    "isAdmin" to false,
                                    "lastLogin" to Instant.now().toString()
                                )).toString())
                            }.apply()
                        }
                        return@withContext mapOf("success" to true, "user" to data)
                    }
                }
                return@withContext mapOf("success" to false, "message" to "Authentication error: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("success" to false, "message" to (e.message ?: "Authentication error"))
        }
    }

    suspend fun register(userData: Map<String, Any>): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(userData).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/auth")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val data = JSONObject(responseBody).toMap()
                        val token = data["token"]?.toString()
                        if (token != null && token.isNotEmpty()) {
                            prefs.edit().apply {
                                putString("authToken", token)
                                putString("currentUser", JSONObject(mapOf(
                                    "id" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "plan" to data["plan"]
                                )).toString())
                                putString("currentSession", JSONObject(mapOf(
                                    "userId" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "plan" to data["plan"],
                                    "isLoggedIn" to true,
                                    "isAdmin" to false
                                )).toString())
                            }.apply()
                        }
                        return@withContext mapOf("success" to true, "user" to data)
                    }
                }
                return@withContext mapOf("success" to false, "message" to "Registration error: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("success" to false, "message" to (e.message ?: "Registration error"))
        }
    }

    suspend fun loginAdmin(email: String, password: String): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject()
                .put("email", email)
                .put("password", password)
                .toString()
                .toRequestBody(JSON)

            val request = Request.Builder()
                .url("${Environment.baseUrl}/admin_user/login")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val data = JSONObject(responseBody).toMap()
                        val role = data["role"]?.toString()
                        val isAdmin = role == "admin" || data["isAdmin"] == true || (data["email"]?.toString()?.contains("admin") == true)
                        if (!isAdmin) {
                            return@withContext mapOf("success" to false, "message" to "You do not have administrator permissions")
                        }
                        val token = data["token"]?.toString()
                        if (token != null && token.isNotEmpty()) {
                            prefs.edit().apply {
                                putString("authToken", token)
                                putString("currentUser", JSONObject(mapOf(
                                    "id" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "isAdmin" to true
                                )).toString())
                                putString("adminToken", "admin_session")
                                putString("currentAdmin", JSONObject(data).toString())
                                putString("currentSession", JSONObject(mapOf(
                                    "userId" to data["id"],
                                    "name" to data["name"],
                                    "email" to data["email"],
                                    "isLoggedIn" to true,
                                    "isAdmin" to true,
                                    "lastLogin" to Instant.now().toString()
                                )).toString())
                            }.apply()
                        }
                        return@withContext mapOf("success" to true, "user" to data, "isAdmin" to true)
                    }
                }
                return@withContext mapOf("success" to false, "message" to "Administrator authentication error: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("success" to false, "message" to (e.message ?: "Administrator authentication error"))
        }
    }

    suspend fun refreshSession(): Boolean = withContext(Dispatchers.IO) {
        try {
            val token = prefs.getString("authToken", null) ?: return@withContext false

            val request = Request.Builder()
                .url("${Environment.baseUrl}/auth/me")
                .addHeader("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val data = JSONObject(responseBody).toMap()
                        prefs.edit().apply {
                            putString("currentUser", JSONObject(data).toString())
                            putString("currentSession", JSONObject(mapOf(
                                "userId" to data["id"],
                                "name" to data["name"],
                                "email" to data["email"],
                                "isLoggedIn" to true,
                                "isAdmin" to (data["isAdmin"] == true),
                                "lastLogin" to Instant.now().toString()
                            )).toString())
                        }.apply()
                        return@withContext true
                    }
                }
                return@withContext false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        prefs.edit().apply {
            remove("authToken")
            remove("currentUser")
            remove("adminToken")
            remove("currentAdmin")
            remove("currentSession")
        }.apply()
    }
}

private fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    val keys = keys()
    while (keys.hasNext()) {
        val key = keys.next()
        val value = get(key)
        map[key] = if (value is JSONObject) value.toMap() else value
    }
    return map
}
