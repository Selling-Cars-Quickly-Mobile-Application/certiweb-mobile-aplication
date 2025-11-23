package pe.edu.upc.certiweb_mobile_application.certifications.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import pe.edu.upc.certiweb_mobile_application.config.DioClient
import pe.edu.upc.certiweb_mobile_application.environments.Environment

class ReservationService {
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun createReservation(data: Map<String, Any?>): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(data).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Reservation creation failed: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    suspend fun getAllReservations(): List<Map<String, Any?>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONArray(responseBody).toListOfMaps()
                    }
                }
                throw Exception("Failed to get reservations: ${response.code}")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getReservationById(id: String): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations/$id")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Failed to get reservation: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    suspend fun getReservationsByUserId(userId: Int): List<Map<String, Any?>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations/user/$userId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONArray(responseBody).toListOfMaps()
                    }
                }
                throw Exception("Failed to get user reservations: ${response.code}")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateReservationStatus(id: String, status: String): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject().put("status", status).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/reservations/$id")
                .put(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Failed to update reservation status: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
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