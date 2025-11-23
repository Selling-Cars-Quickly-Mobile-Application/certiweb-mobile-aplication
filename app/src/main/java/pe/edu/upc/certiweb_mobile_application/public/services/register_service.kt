package pe.edu.upc.certiweb_mobile_application.public.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import pe.edu.upc.certiweb_mobile_application.config.DioClient
import pe.edu.upc.certiweb_mobile_application.environments.Environment

class RegisterService {
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun registerUser(data: Map<String, Any?>): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(data).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/users")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Registration failed: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    private fun JSONObject.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = get(key)
            map[key] = when (value) {
                is JSONObject -> value.toMap()
                is JSONArray -> value.toListOfMaps()
                else -> value
            }
        }
        return map
    }

    private fun JSONArray.toListOfMaps(): List<Map<String, Any?>> {
        val list = mutableListOf<Map<String, Any?>>()
        for (i in 0 until length()) {
            val value = get(i)
            if (value is JSONObject) {
                list.add(value.toMap())
            }
        }
        return list
    }
}