package pe.edu.upc.certiweb_mobile_application.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val plan: String
)

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String, val plan: String = "Free")