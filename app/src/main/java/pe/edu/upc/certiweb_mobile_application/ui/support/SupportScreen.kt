package pe.edu.upc.certiweb_mobile_application.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.certiweb_mobile_application.ui.theme.*

@Composable
fun SupportScreen(navController: NavHostController) {
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE2A74A), Color(0xFFD48B25))
                        ),
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEAD7B1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.HelpOutline, contentDescription = null, tint = Color(0xFF7A5E2E))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Support Page",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Welcome to our support page. Here you'll find help to solve your doubts.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                ContactCard(
                    icon = { Icon(Icons.Filled.Email, contentDescription = null, tint = GreenPrimary) },
                    title = "Email:",
                    subtitle = "soporte@certiweb.com"
                )
                Spacer(Modifier.height(12.dp))
                ContactCard(
                    icon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = GreenPrimary) },
                    title = "Phone:",
                    subtitle = "+123 456 7890"
                )
                Spacer(Modifier.height(12.dp))
                ContactCard(
                    icon = { Icon(Icons.Filled.AccessTime, contentDescription = null, tint = GreenPrimary) },
                    title = "Business hours:",
                    subtitle = "Monday to Friday from 9:00 to 18:00"
                )

                Spacer(Modifier.height(16.dp))

                InfoCard(
                    text = "You can also check our Frequently Asked Questions (FAQ) section for quick answers."
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD48B25))
                ) {
                    Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Back to Home", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ContactCard(icon: @Composable () -> Unit, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF2E6D3)),
                contentAlignment = Alignment.Center
            ) { icon() }

            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun InfoCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = TextSecondary,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}