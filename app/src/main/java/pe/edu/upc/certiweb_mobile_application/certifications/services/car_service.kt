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

class CarService {
    private val client: OkHttpClient = DioClient.okHttpClient
    private val JSON = "application/json; charset=utf-8".toMediaType()

    suspend fun createCar(data: Map<String, Any?>): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(data).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/cars")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Car creation failed: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    suspend fun getAllCars(): List<Map<String, Any?>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/cars")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONArray(responseBody).toListOfMaps()
                    }
                }
                throw Exception("Failed to get cars: ${response.code}")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCarById(id: String): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/cars/$id")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Failed to get car: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    suspend fun updateCar(id: String, data: Map<String, Any?>): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val requestBody = JSONObject(data).toString().toRequestBody(JSON)
            val request = Request.Builder()
                .url("${Environment.baseUrl}/cars/$id")
                .patch(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Car update failed: ${response.code}")
            }
        } catch (e: Exception) {
            mapOf("error" to (e.message ?: "Unknown error"))
        }
    }

    suspend fun getCarPdf(id: String): Map<String, Any?> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Environment.baseUrl}/cars/$id/pdf")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        return@withContext JSONObject(responseBody).toMap()
                    }
                }
                throw Exception("Failed to get car PDF: ${response.code}")
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