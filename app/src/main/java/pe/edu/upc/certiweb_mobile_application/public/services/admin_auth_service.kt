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

class AdminAuthService(private val context: Context) {
    private val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun login(email: String, password: String): Map<String, Any?> = withContext(Dispatchers.IO) {
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
                        val token = data["token"]?.toString()
                        if (token != null && token.isNotEmpty()) {
                            prefs.edit().apply {
                                putString("authToken", token)
                                putString("adminToken", "admin_session")
                                putString("currentAdmin", (data["user"]?.toString() ?: email))
                            }.apply()
                        }
                        return@withContext data
                    }
                }
                throw Exception("Admin login failed: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    fun logout() {
        prefs.edit().apply {
            remove("adminToken")
            remove("currentAdmin")
        }.apply()
    }

    suspend fun hasSession(): Boolean = withContext(Dispatchers.IO) {
        prefs.getString("adminToken", "")?.isNotEmpty() ?: false
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