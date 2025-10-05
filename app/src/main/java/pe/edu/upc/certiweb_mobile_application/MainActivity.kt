package pe.edu.upc.certiweb_mobile_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.certiweb_mobile_application.ui.theme.CertiwebmobileapplicationTheme
import pe.edu.upc.certiweb_mobile_application.ui.home.HomeScreen
import pe.edu.upc.certiweb_mobile_application.ui.auth.AuthViewModel
import pe.edu.upc.certiweb_mobile_application.ui.auth.LoginScreen
import pe.edu.upc.certiweb_mobile_application.ui.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CertiwebmobileapplicationTheme { AppNav() }
        }
    }
}

@Composable
fun AppNav() {
    val navController: NavHostController = rememberNavController()
    val vm = remember { AuthViewModel() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(vm, onLoggedIn = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }, onNavigateRegister = { navController.navigate("register") })
        }
        composable("register") {
            RegisterScreen(vm, onRegistered = {
                navController.navigate("home") {
                    popUpTo("register") { inclusive = true }
                }
            }, onNavigateLogin = { navController.popBackStack() })
        }
        composable("home") {
            HomeScreen(onLogout = {
                vm.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }
    }
}