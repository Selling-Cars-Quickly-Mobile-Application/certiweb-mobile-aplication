package pe.edu.upc.certiweb_mobile_application

import android.app.Application
import android.util.Log
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.plugin.common.MethodChannel
import pe.edu.upc.certiweb_mobile_application.data.SessionManager
import android.content.Context

class CertiwebApp : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            val native = getSharedPreferences("certiweb_session", MODE_PRIVATE)
            val flutter = getSharedPreferences("FlutterSharedPreferences", MODE_PRIVATE)
            val email = native.getString("user_email", null)
            Log.i("CertiwebApp", "Sincronización inicial - email encontrado: ${email ?: "null"}")
            if (!email.isNullOrEmpty()) {
                val id = native.getString("user_id", "") ?: ""
                val name = native.getString("user_name", "") ?: ""
                val plan = native.getString("user_plan", "Free") ?: "Free"
                Log.i("CertiwebApp", "Sincronizando usuario: id=$id, name=$name, email=$email, plan=$plan")
                flutter.edit()
                    .putString("flutter.user_id", id)
                    .putString("flutter.user_name", name)
                    .putString("flutter.user_email", email)
                    .putString("flutter.user_plan", plan)
                    .apply()
                Log.i("CertiwebApp", "Sincronización completada")
            } else {
                Log.w("CertiwebApp", "No se encontró usuario para sincronizar")
            }
        } catch (e: Exception) {
            Log.e("CertiwebApp", "Error en sincronización inicial", e)
        }

        val engine = FlutterEngine(this)
        GeneratedPluginRegistrant.registerWith(engine)
        engine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        val sessionManager = SessionManager(applicationContext)
        val channel = MethodChannel(engine.dartExecutor.binaryMessenger, "reservation_channel")
        Log.i("CertiwebApp", "MethodChannel 'reservation_channel' registrado en CertiwebApp")
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getUserData" -> {
                    Log.i("CertiwebApp", "Invocado getUserData desde Flutter")
                    val u = sessionManager.getCachedUser()
                    if (u != null) {
                        val payload: HashMap<String, Any?> = hashMapOf(
                            "id" to u.id,
                            "name" to u.name,
                            "email" to u.email
                        )
                        Log.i("CertiwebApp", "Devolviendo usuario cacheado: id=${u.id}")
                        result.success(payload)
                    } else {
                        Log.w("CertiwebApp", "No hay usuario cacheado en SessionManager")
                        result.error("NO_USER", "User not logged in", null)
                    }
                }
                "getUserPrefs" -> {
                    Log.i("CertiwebApp", "Invocado getUserPrefs desde Flutter (fallback)")
                    val fp = getSharedPreferences("FlutterSharedPreferences", MODE_PRIVATE)
                    val email = fp.getString("flutter.user_email", null)
                    if (!email.isNullOrEmpty()) {
                        val id = fp.getString("flutter.user_id", "") ?: ""
                        val name = fp.getString("flutter.user_name", "") ?: ""
                        val payload: HashMap<String, Any?> = hashMapOf(
                            "id" to id,
                            "name" to name,
                            "email" to email
                        )
                        Log.i("CertiwebApp", "Devolviendo usuario desde FlutterSharedPreferences: id=$id")
                        result.success(payload)
                    } else {
                        Log.w("CertiwebApp", "FlutterSharedPreferences no contiene usuario")
                        result.error("NO_USER_PREFS", "No user in FlutterSharedPreferences", null)
                    }
                }
                "getReservationPrefill" -> {
                    result.success(null)
                }
                "navigateToDashboard" -> {
                    Log.i("CertiwebApp", "Invocado navigateToDashboard - No se puede manejar aquí")
                    result.success(false)
                }
                else -> result.notImplemented()
            }
        }

        FlutterEngineCache.getInstance().put("reservation_engine", engine)
    }
}