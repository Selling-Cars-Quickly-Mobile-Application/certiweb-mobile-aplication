package pe.edu.upc.certiweb_mobile_application.public.pages.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import pe.edu.upc.certiweb_mobile_application.public.services.AuthService
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background

@Composable
fun RegisterScreen(onRegistered: () -> Unit, onNavigateLogin: () -> Unit) {
    val ctx = LocalContext.current
    val auth = remember { AuthService(ctx) }
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf<String?>(null) }
    var step by remember { mutableStateOf(0) }
    var selectedPlan by remember { mutableStateOf<String?>(null) }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

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
        Card(modifier = Modifier.padding(16.dp).fillMaxWidth(0.95f)) {
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
                        text = "Crear Cuenta",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("Datos personales", "Plan", "Pago").forEachIndexed { i, t ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(shape = MaterialTheme.shapes.large, color = if (i <= step) Color(0xFF1E4D2B) else Color.White, tonalElevation = if (i <= step) 4.dp else 0.dp, shadowElevation = if (i <= step) 6.dp else 0.dp) {
                                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                    Text(text = "${i + 1}", color = if (i <= step) Color.White else Color(0xFF64748B))
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = t, style = MaterialTheme.typography.bodySmall, color = if (i <= step) Color(0xFF1E293B) else Color(0xFF64748B))
                        }
                    }
                }
                when (step) {
                    0 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                        }
                    }
                    1 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("Free", "Mensual", "Anual").forEach { p ->
                                OutlinedButton(
                                    onClick = { selectedPlan = p },
                                    modifier = Modifier.fillMaxWidth(),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(width = if (selectedPlan == p) 3.dp else 2.dp)
                                ) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(p)
                                        if (selectedPlan == p) Text("Seleccionado")
                                    }
                                }
                            }
                        }
                    }
                    2 -> {
                        if (selectedPlan == "Free") {
                            Surface(color = Color(0xFF16A34A).copy(alpha = 0.1f)) {
                                Text("Este plan es gratuito, no se requiere información de pago", color = Color(0xFF16A34A), modifier = Modifier.padding(16.dp))
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = { Text("Número de Tarjeta") }, modifier = Modifier.fillMaxWidth())
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    OutlinedTextField(value = expiryDate, onValueChange = { expiryDate = it }, label = { Text("Fecha de Expiración") }, modifier = Modifier.weight(1f))
                                    OutlinedTextField(value = cvv, onValueChange = { cvv = it }, label = { Text("CVV") }, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                if (error != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626).copy(alpha = 0.1f))) {
                        Row(modifier = Modifier.padding(12.dp)) { Text(text = error ?: "", color = Color(0xFFDC2626)) }
                    }
                }
                if (success != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF16A34A).copy(alpha = 0.1f))) {
                        Row(modifier = Modifier.padding(12.dp)) { Text(text = success ?: "", color = Color(0xFF16A34A)) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (step > 0) {
                        OutlinedButton(onClick = {
                            error = null
                            success = null
                            step--
                        }, modifier = Modifier.weight(1f)) { Text("Anterior") }
                    }
                    Button(onClick = {
                        error = null
                        success = null
                        if (step == 0) {
                            if (name.isBlank() || email.isBlank() || password.isBlank() || !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email)) {
                                error = "Completa los datos correctamente"
                                return@Button
                            }
                            step++
                        } else if (step == 1) {
                            if (selectedPlan == null) {
                                error = "Selecciona un plan"
                                return@Button
                            }
                            step++
                        } else {
                            loading = true
                            val plan = selectedPlan ?: "Free"
                            if (plan != "Free" && (cardNumber.isBlank() || expiryDate.isBlank() || cvv.isBlank())) {
                                error = "Completa la información de pago"
                                loading = false
                                return@Button
                            }
                            scope.launch {
                                val userData = mapOf(
                                    "name" to name.trim(),
                                    "email" to email.trim(),
                                    "password" to password,
                                    "plan" to plan
                                )
                                val result = auth.register(userData)
                                if (result["success"] as? Boolean == true) {
                                    success = "Registro exitoso"
                                    onRegistered()
                                } else {
                                    error = (result["message"] ?: "Registro fallido").toString()
                                }
                                loading = false
                            }
                        }
                    }, modifier = Modifier.weight(1f), enabled = !loading) { Text(if (step == 2) "Completar" else "Siguiente") }
                }
                TextButton(onClick = onNavigateLogin) { Text("Log In") }
            }
        }
    }
}