package pe.edu.upc.certiweb_mobile_application.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(vm: AuthViewModel, onLoggedIn: () -> Unit, onNavigateRegister: () -> Unit) {
    val state by vm.state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (state.user != null) {
        LaunchedEffect(state.user) { onLoggedIn() }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.padding(16.dp).fillMaxWidth(0.9f)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Login", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
                if (state.error != null) Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
                Button(onClick = { vm.login(email.trim(), password) }, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) { Text("Login") }
                TextButton(onClick = onNavigateRegister) { Text("Register") }
            }
        }
    }
}