package pe.edu.upc.certiweb_mobile_application.public.pages.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pe.edu.upc.certiweb_mobile_application.public.services.AuthService
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

@Composable
fun LoginScreen(onLoggedIn: () -> Unit, onAdminLoggedIn: () -> Unit, onNavigateRegister: () -> Unit) {
    val ctx = LocalContext.current
    val auth = remember { AuthService(ctx) }
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF1E4D2B), Color(0xFF2D6B3F))
                            )
                        )
                ) {
                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(32.dp)
                    )
                }
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                    if (error != null) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.1f))) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Text(text = error ?: "", color = Color(0xFFFF5252))
                            }
                        }
                    }
                    if (success != null) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f))) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Text(text = success ?: "", color = Color(0xFF4CAF50))
                            }
                        }
                    }
                    ElevatedButton(
                        onClick = {
                            loading = true
                            error = null
                            success = null
                            if (email.isBlank() || password.isBlank() || !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email)) {
                                error = "Por favor, completa los campos con datos válidos."
                                loading = false
                            } else {
                                scope.launch {
                                    val userRes = auth.login(email.trim(), password)
                                    if (userRes["success"] as? Boolean == true) {
                                        success = "Inicio de sesión exitoso"
                                        onLoggedIn()
                                    } else {
                                        val adminRes = auth.loginAdmin(email.trim(), password)
                                        if (adminRes["success"] as? Boolean == true) {
                                            success = "Inicio de sesión de administrador"
                                            onAdminLoggedIn()
                                        } else {
                                            error = "Credenciales incorrectas. Verifica correo y contraseña."
                                        }
                                    }
                                    loading = false
                                }
                            }
                        },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E4D2B))
                    ) {
                        Text(if (loading) "Iniciando sesión..." else "Entrar", color = Color.White)
                    }
                    TextButton(onClick = onNavigateRegister) { Text("Regístrate", color = Color(0xFF1E4D2B)) }
                }
            }
        }
    }
}