package pe.edu.upc.certiweb_mobile_application.ui.certifycar

import android.app.Activity
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import pe.edu.upc.certiweb_mobile_application.data.SessionManager

class ReservationFlutterActivity : FlutterActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(applicationContext)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "reservation_channel")
        Log.i("ReservationFlutter", "MethodChannel 'reservation_channel' registrado en configureFlutterEngine")

        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getUserData" -> {
                    Log.i("ReservationFlutter", "Invocado getUserData desde Flutter")
                    val u = sessionManager.getCachedUser()
                    if (u != null) {
                        val payload: HashMap<String, Any?> = hashMapOf(
                            "id" to u.id,
                            "name" to u.name,
                            "email" to u.email
                        )
                        Log.i("ReservationFlutter", "Devolviendo usuario cacheado: id=${u.id}")
                        result.success(payload)
                    } else {
                        Log.w("ReservationFlutter", "No hay usuario cacheado en SessionManager")
                        result.error("NO_USER", "User not logged in", null)
                    }
                }
                "getUserPrefs" -> {
                    Log.i("ReservationFlutter", "Invocado getUserPrefs desde Flutter (fallback)")
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
                        Log.i("ReservationFlutter", "Devolviendo usuario desde FlutterSharedPreferences: id=$id")
                        result.success(payload)
                    } else {
                        Log.w("ReservationFlutter", "FlutterSharedPreferences no contiene usuario")
                        result.error("NO_USER_PREFS", "No user in FlutterSharedPreferences", null)
                    }
                }
                "getReservationPrefill" -> {
                    result.success(null)
                }
                "navigateToDashboard" -> {
                    Log.i("ReservationFlutter", "Invocado navigateToDashboard - Cerrando Flutter y retornando OK")
                    setResult(Activity.RESULT_OK)
                    finish()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }
}