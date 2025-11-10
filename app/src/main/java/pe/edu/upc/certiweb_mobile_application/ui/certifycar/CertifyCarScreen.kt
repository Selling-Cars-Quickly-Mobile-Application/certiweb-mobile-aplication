package pe.edu.upc.certiweb_mobile_application.ui.certifycar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pe.edu.upc.certiweb_mobile_application.data.SessionManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.engine.dart.DartExecutor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertifyCarScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context.applicationContext)
    
    val currentUser = sessionManager.getCachedUser()
    Log.i("CertifyCar", "Usuario actual en CertifyCarScreen: ${currentUser?.toString() ?: "null"}")

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            navController.navigate("home") {
                popUpTo("certifyCar") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(Unit) {
        var engine = FlutterEngineCache.getInstance().get("reservation_engine")
        if (engine == null) {
            engine = FlutterEngine(context)
            engine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
            FlutterEngineCache.getInstance().put("reservation_engine", engine)
        }

        val intent = Intent(context, ReservationFlutterActivity::class.java)
            .putExtra("cached_engine_id", "reservation_engine")
        launcher.launch(intent)
    }

    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Abriendo reservación en Flutter...")
    }
}