package pe.edu.upc.certiweb_mobile_application.certifications.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background

@Composable
fun DashboardScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFF5F0E1), Color(0xFFEDE4D1))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(24.dp).fillMaxWidth(0.9f)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
                Surface(color = Color(0xFF16A34A).copy(alpha = 0.1f)) {
                    Text("Redirección exitosa", color = Color(0xFF16A34A), modifier = Modifier.padding(16.dp))
                }
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
            }
        }
    }
}