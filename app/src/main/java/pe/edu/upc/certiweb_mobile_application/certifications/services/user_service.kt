package pe.edu.upc.certiweb_mobile_application.certifications.services

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pe.edu.upc.certiweb_mobile_application.config.DioClient
import pe.edu.upc.certiweb_mobile_application.environments.Environment

class UserService(private val context: Context) {
    private val prefs = context.getSharedPreferences("CertiwebPreferences", Context.MODE_PRIVATE)
    private val client: OkHttpClient = DioClient.okHttpClient

    suspend fun getCurrentUser(): Result<Map<String, Any?>> = withContext(Dispatchers.IO) {
        val sessionStr = prefs.getString("currentSession", null)
        if (sessionStr == null) {
            return@withContext Result.failure(Exception("No active session"))
        }
        
        var session = mapOf<String, Any?>()
        try {
            session = JSONObject(sessionStr).toMap()
        } catch (_: Exception) {}
        
        val userId = session["userId"]?.toString()
        val email = session["email"]?.toString()
        
        try {
            if (userId != null) {
                val request = Request.Builder()
                    .url("${Environment.baseUrl}/users/$userId")
                    .get()
                    .build()
                
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val data = JSONObject(responseBody).toMap()
                            val finalData = data.toMutableMap()
                            finalData["id"] = finalData["id"] ?: userId
                            return@withContext Result.success(finalData)
                        }
                    }
                }
            }
        } catch (_: Exception) {}
        
        if (email != null) {
            try {
                val request = Request.Builder()
                    .url("${Environment.baseUrl}/users?email=$email")
                    .get()
                    .build()
                
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            val list = JSONObject(responseBody).toList()
                            if (list.isNotEmpty()) {
                                return@withContext Result.success(list.first())
                            }
                        }
                    }
                }
            } catch (_: Exception) {}
        }
        
        val name = session["name"]?.toString()
        if (name != null && userId != null) {
            return@withContext Result.success(mapOf(
                "id" to userId,
                "name" to name,
                "email" to (email ?: "")
            ))
        }
        
        return@withContext Result.failure(Exception("User not found"))
    }

    fun logout() {
        val e = prefs.edit()
        e.remove("currentSession")
        e.remove("authToken")
        e.remove("currentUser")
        e.remove("adminToken")
        e.remove("currentAdmin")
        e.apply()
    }
}

private fun JSONObject.toMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    val keys = this.keys()
    while (keys.hasNext()) {
        val key = keys.next()
        when (val value = this.get(key)) {
            is JSONObject -> map[key] = value.toMap()
            else -> map[key] = value
        }
    }
    return map
}

private fun JSONObject.toList(): List<Map<String, Any?>> {
    val list = mutableListOf<Map<String, Any?>>()
    if (this.length() == 0) return list
    
    // Si es un array JSON
    try {
        for (i in 0 until this.length()) {
            val item = this.get(i.toString())
            if (item is JSONObject) {
                list.add(item.toMap())
            }
        }
    } catch (_: Exception) {
        // Si no es un array, intentar como objeto único
        try {
            list.add(this.toMap())
        } catch (_: Exception) {}
    }
    
    return list
}