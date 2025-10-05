package pe.edu.upc.certiweb_mobile_application.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.edu.upc.certiweb_mobile_application.data.AuthRepository
import pe.edu.upc.certiweb_mobile_application.data.model.RegisterRequest
import pe.edu.upc.certiweb_mobile_application.data.model.User

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
    var state = androidx.compose.runtime.mutableStateOf(AuthState())
        private set

    fun login(email: String, password: String) {
        state.value = state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = repo.login(email, password)
            state.value = result.fold(
                onSuccess = { state.value.copy(isLoading = false, user = it) },
                onFailure = { state.value.copy(isLoading = false, error = it.message) }
            )
        }
    }

    fun register(name: String, email: String, password: String, plan: String = "Free") {
        state.value = state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = repo.register(RegisterRequest(name, email, password, plan))
            state.value = result.fold(
                onSuccess = { state.value.copy(isLoading = false, user = it) },
                onFailure = { state.value.copy(isLoading = false, error = it.message) }
            )
        }
    }

    fun logout() {
        state.value = AuthState(isLoading = false, user = null, error = null)
    }
}