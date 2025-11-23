package pe.edu.upc.certiweb_mobile_application.shared.services

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import pe.edu.upc.certiweb_mobile_application.environments.Environment
import java.io.File

class ImgbbApiService {
    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    suspend fun uploadImage(filePath: String): Map<String, Any> {
        val file = File(filePath)
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                name = "image",
                filename = file.name,
                body = RequestBody.create("application/octet-stream".toMediaType(), file)
            )
            .build()
        val url = "https://api.imgbb.com/1/upload?key=${Environment.imgbbApiKey}"
        val req = Request.Builder().url(url).post(body).build()
        client.newCall(req).execute().use { res ->
            val json = JSONObject(res.body?.string() ?: "{}")
            val data = json.optJSONObject("data") ?: JSONObject()
            val map = mutableMapOf<String, Any>()
            for (key in data.keys()) {
                map[key] = data.get(key)
            }
            return map
        }
    }

    suspend fun uploadImageBytes(bytes: ByteArray, filename: String): Map<String, Any> {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                name = "image",
                filename = filename,
                body = RequestBody.create("application/octet-stream".toMediaType(), bytes)
            )
            .build()
        val url = "https://api.imgbb.com/1/upload?key=${Environment.imgbbApiKey}"
        val req = Request.Builder().url(url).post(body).build()
        client.newCall(req).execute().use { res ->
            val json = JSONObject(res.body?.string() ?: "{}")
            val data = json.optJSONObject("data") ?: JSONObject()
            val map = mutableMapOf<String, Any>()
            for (key in data.keys()) {
                map[key] = data.get(key)
            }
            return map
        }
    }
}