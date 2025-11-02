package pe.edu.upc.certiweb_mobile_application.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.IntrinsicSize
import pe.edu.upc.certiweb_mobile_application.ui.theme.CreamBackground
import pe.edu.upc.certiweb_mobile_application.ui.theme.CardBackground
import pe.edu.upc.certiweb_mobile_application.ui.theme.TextPrimary
import pe.edu.upc.certiweb_mobile_application.ui.theme.TextSecondary
import pe.edu.upc.certiweb_mobile_application.ui.theme.GreenPrimary

@Composable
fun ProfileScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    val vm: ProfileViewModel = viewModel(factory = ProfileViewModel.factory(ctx))
    val state by vm.state

    LaunchedEffect(Unit) { vm.load() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        state.user?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    // Header verde con título
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GreenPrimary)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("User Profile", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoItem(label = "Name:", value = user.name)
                        InfoItem(label = "Email:", value = user.email)
                        InfoItem(label = "Plan:", value = user.plan)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Home, contentDescription = null, tint = GreenPrimary)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Back to home",
                                color = TextPrimary,
                                modifier = Modifier.clickable { navController.navigate("home") }
                            )
                        }
                    }
                }
            }
        } ?: run {
            Text(text = "No hay datos de usuario", color = MaterialTheme.colorScheme.error)
            if (state.error != null) {
                Spacer(Modifier.height(8.dp))
                Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = CardBackground), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(GreenPrimary))
            Column(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = label, style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
            }
        }
    }
}