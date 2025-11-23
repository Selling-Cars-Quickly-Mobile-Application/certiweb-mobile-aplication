package pe.edu.upc.certiweb_mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.certiweb_mobile_application.ui.theme.CertiwebmobileapplicationTheme
import pe.edu.upc.certiweb_mobile_application.public.pages.login.LoginScreen
import pe.edu.upc.certiweb_mobile_application.public.pages.register.RegisterScreen
import pe.edu.upc.certiweb_mobile_application.config.DioClient
import pe.edu.upc.certiweb_mobile_application.certifications.components.dashboard.DashboardScreen

class CertiwebMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DioClient.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            CertiwebmobileapplicationTheme { AppNavK() }
        }
    }
}

@Composable
fun AppNavK() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onAdminLoggedIn = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegistered = {
                    navController.navigate("dashboard") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateLogin = { navController.popBackStack() }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onBack = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}
