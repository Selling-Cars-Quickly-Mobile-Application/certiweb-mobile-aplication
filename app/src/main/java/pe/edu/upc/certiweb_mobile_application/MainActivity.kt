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
import pe.edu.upc.certiweb_mobile_application.ui.certifiedcars.CertifiedCarsScreen
import pe.edu.upc.certiweb_mobile_application.ui.certifycar.CertifyCarScreen
import pe.edu.upc.certiweb_mobile_application.ui.profile.ProfileScreen
import pe.edu.upc.certiweb_mobile_application.ui.history.HistoryScreen
import pe.edu.upc.certiweb_mobile_application.ui.support.SupportScreen
import pe.edu.upc.certiweb_mobile_application.ui.termsofuse.TermsOfUseScreen

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
    val ctx = androidx.compose.ui.platform.LocalContext.current

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(vm, onLoggedIn = {
                vm.state.value.user?.let { pe.edu.upc.certiweb_mobile_application.data.SessionManager(ctx.applicationContext).saveUser(it) }
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }, onNavigateRegister = { navController.navigate("register") })
        }
        composable("register") {
            RegisterScreen(vm, onRegistered = {
                vm.state.value.user?.let { pe.edu.upc.certiweb_mobile_application.data.SessionManager(ctx.applicationContext).saveUser(it) }
                navController.navigate("home") {
                    popUpTo("register") { inclusive = true }
                }
            }, onNavigateLogin = { navController.popBackStack() })
        }
        composable("home") {
            HomeScreen(navController = navController, onLogout = {
                vm.logout()
                pe.edu.upc.certiweb_mobile_application.data.SessionManager(ctx.applicationContext).clear()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }
        composable("certifiedCars") { CertifiedCarsScreen() }
        composable("certifyCar") { CertifyCarScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("history") { HistoryScreen() }
        composable("support") { SupportScreen(navController) }
        composable("termsOfUse") { TermsOfUseScreen(navController) }
    }
}