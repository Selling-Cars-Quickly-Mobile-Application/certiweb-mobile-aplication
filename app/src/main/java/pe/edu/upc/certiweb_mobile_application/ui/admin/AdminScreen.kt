package pe.edu.upc.certiweb_mobile_application.ui.admin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.FlutterInjector
import android.app.Activity
import android.content.Intent
import android.util.Log

@Composable
fun AdminScreen() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ -> }

    LaunchedEffect(Unit) {
        try {
            var engine = FlutterEngineCache.getInstance().get("admin_engine")
            if (engine == null) {
                engine = FlutterEngine(context)
                io.flutter.plugins.GeneratedPluginRegistrant.registerWith(engine)
                val loader = FlutterInjector.instance().flutterLoader()
                try { loader.startInitialization(context) } catch (_: Exception) {}
                try { loader.ensureInitializationComplete(context, null) } catch (_: Exception) {}
                val appBundlePath = FlutterInjector.instance().flutterLoader().findAppBundlePath()
                engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint(appBundlePath, "mainAdmin"))
                FlutterEngineCache.getInstance().put("admin_engine", engine)
            } else if (!engine.dartExecutor.isExecutingDart) {
                val appBundlePath = FlutterInjector.instance().flutterLoader().findAppBundlePath()
                engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint(appBundlePath, "mainAdmin"))
            }

            val intent = FlutterActivity
                .CachedEngineIntentBuilder(AdminFlutterActivity::class.java, "admin_engine")
                .build(context)
            launcher.launch(intent)
        } catch (e: Exception) {
            Log.e("AdminScreen", "No se pudo lanzar AdminFlutterActivity con engine cacheado", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Abriendo Admin Certification en Flutter...", style = MaterialTheme.typography.headlineMedium)
    }
}
