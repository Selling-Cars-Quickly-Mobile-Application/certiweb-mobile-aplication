package pe.edu.upc.certiweb_mobile_application.data

import android.content.Context
import pe.edu.upc.certiweb_mobile_application.data.model.User

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("certiweb_session", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_email", user.email)
            .putString("user_plan", user.plan)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun getId(): String? = prefs.getString("user_id", null)
    fun getEmail(): String? = prefs.getString("user_email", null)

    fun getCachedUser(): User? {
        val email = getEmail() ?: return null
        val id = getId() ?: ""
        val name = prefs.getString("user_name", "") ?: ""
        val plan = prefs.getString("user_plan", "Free") ?: "Free"
        return User(id = id, name = name, email = email, plan = plan)
    }
}