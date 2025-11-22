package pe.edu.upc.certiweb_mobile_application.ui.admin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import pe.edu.upc.certiweb_mobile_application.data.SessionManager

class AdminFlutterActivity : FlutterActivity() {

    private lateinit var sessionManager: SessionManager
    private fun setupChannels(engine: FlutterEngine) {
        val channel = MethodChannel(engine.dartExecutor.binaryMessenger, "admin_channel")
        val bridge = MethodChannel(engine.dartExecutor.binaryMessenger, "android_bridge")
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "getUserData" -> {
                    val u = sessionManager.getCachedUser()
                    if (u != null) {
                        val payload: HashMap<String, Any?> = hashMapOf(
                            "id" to u.id,
                            "name" to u.name,
                            "email" to u.email
                        )
                        result.success(payload)
                    } else {
                        result.error("NO_USER", "User not logged in", null)
                    }
                }
                "getUserPrefs" -> {
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
                        result.success(payload)
                    } else {
                        result.error("NO_USER_PREFS", "No user in FlutterSharedPreferences", null)
                    }
                }
                "logout" -> {
                    try {
                        sessionManager.clear()
                        val intent = android.content.Intent(this, pe.edu.upc.certiweb_mobile_application.MainActivity::class.java)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        setResult(Activity.RESULT_OK)
                        finish()
                        result.success(true)
                    } catch (e: Exception) {
                        result.error("LOGOUT_ERROR", e.message, null)
                    }
                }
                "navigateToDashboard" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }

        bridge.setMethodCallHandler { call, result ->
            when (call.method) {
                "getInitialParams" -> {
                    val params = intent?.getStringExtra("initial_params") ?: "{}"
                    result.success(params)
                }
                "close" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                    result.success(true)
                }
                "log" -> {
                    val msg = call.argument<String>("message") ?: ""
                    android.util.Log.i("FlutterBridge", msg)
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(applicationContext)
        try {
            val engine = flutterEngine
            if (engine != null) {
                setupChannels(engine)
            }
        } catch (_: Exception) {}
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        io.flutter.plugins.GeneratedPluginRegistrant.registerWith(flutterEngine)
        setupChannels(flutterEngine)
    }

    override fun getDartEntrypointFunctionName(): String {
        return "mainAdmin"
    }
}
