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
        FlutterEngineCache.getInstance().put("reservation_engine", engine)

        // Admin engine será inicializado bajo demanda desde AdminScreen
    }
}
