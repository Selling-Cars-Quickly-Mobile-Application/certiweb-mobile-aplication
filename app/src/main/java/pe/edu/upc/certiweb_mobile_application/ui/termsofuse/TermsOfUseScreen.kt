package pe.edu.upc.certiweb_mobile_application.ui.termsofuse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.certiweb_mobile_application.ui.theme.CardBackground
import pe.edu.upc.certiweb_mobile_application.ui.theme.CreamBackground
import pe.edu.upc.certiweb_mobile_application.ui.theme.TextPrimary
import pe.edu.upc.certiweb_mobile_application.ui.theme.TextSecondary

@Composable
fun TermsOfUseScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { TermsHeader() }
        item { IntroNoteCard() }
        item {
            TermItem(
                number = 1,
                title = "Acceptance of Terms",
                body = "By accessing and using CertiWeb (the \"Service\"), you agree to be bound by these Terms and Conditions of Use (\"Terms\"). If you disagree with any part of the terms, you may not access the Service."
            )
        }
        item {
            TermItem(
                number = 2,
                title = "Service Usage",
                body = "The Service is provided to facilitate vehicle information certification and management. You must not use the service for any illegal or unauthorized purpose."
            )
        }
        item {
            TermItem(
                number = 3,
                title = "User Accounts",
                body = "When you create an account with us, you must provide accurate, complete, and up-to-date information at all times. Failure to do so may result in immediate termination of your account on our Service."
            )
        }
        item { BackHomeButton(navController) }
    }
}

@Composable
private fun TermsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE2A74A), Color(0xFFD48B25))
                )
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
                Icon(Icons.Outlined.Article, contentDescription = null, tint = Color(0xFF7A5E2E))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Terms and Conditions of Use",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text("Last update: January 2024", color = Color(0xFFFFF1D0), fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntroNoteCard() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Please read our terms and conditions carefully before using our services.",
                color = TextSecondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermItem(number: Int, title: String, body: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD48B25)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(number.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            Text(body, color = TextSecondary)
        }
    }
}

@Composable
private fun BackHomeButton(navController: NavHostController) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD48B25))
        ) {
            Icon(Icons.Filled.Home, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Back to Home", color = Color.White)
        }
    }
}