package pe.edu.upc.certiweb_mobile_application.config

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import pe.edu.upc.certiweb_mobile_application.environments.Environment

object DioClient {
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val prefs = appContext.getSharedPreferences("FlutterSharedPreferences", Context.MODE_PRIVATE)
        val token = prefs.getString("flutter.authToken", null)
        val req = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }
        }.build()
        val res = chain.proceed(req)
        if (res.code == 401) {
            val e = prefs.edit()
            e.remove("flutter.authToken")
            e.remove("flutter.currentUser")
            e.remove("flutter.adminToken")
            e.remove("flutter.currentAdmin")
            e.remove("flutter.currentSession")
            e.apply()
        }
        res
    }

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Environment.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}