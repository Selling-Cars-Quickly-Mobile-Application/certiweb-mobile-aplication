package pe.edu.upc.certiweb_mobile_application.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.edu.upc.certiweb_mobile_application.data.SessionManager
import pe.edu.upc.certiweb_mobile_application.data.UserRepository
import pe.edu.upc.certiweb_mobile_application.data.model.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val repo: UserRepository = UserRepository(),
    private val session: SessionManager
) : ViewModel() {
    var state = androidx.compose.runtime.mutableStateOf(ProfileState(isLoading = true))
        private set

    fun load() {
        state.value = state.value.copy(isLoading = true, error = null)
        val id = session.getId()
        val email = session.getEmail()
        val cached = session.getCachedUser()
        viewModelScope.launch {
            val result = repo.fetchByIdOrEmail(id, email)
            state.value = result.fold(
                onSuccess = { ProfileState(isLoading = false, user = it, error = null) },
                onFailure = { ProfileState(isLoading = false, user = cached, error = it.message) }
            )
        }
    }

    companion object {
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(session = SessionManager(context.applicationContext)) as T
            }
        }
    }
}