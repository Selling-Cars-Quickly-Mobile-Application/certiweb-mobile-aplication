package pe.edu.upc.certiweb_mobile_application.data

import android.content.Context
import android.util.Log
import pe.edu.upc.certiweb_mobile_application.data.model.User

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("certiweb_session", Context.MODE_PRIVATE)
    
    private val flutterPrefs = context.getSharedPreferences("FlutterSharedPreferences", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_email", user.email)
            .putString("user_plan", user.plan)
            .apply()

        flutterPrefs.edit()
            .putString("flutter.user_id", user.id)
            .putString("flutter.user_name", user.name)
            .putString("flutter.user_email", user.email)
            .putString("flutter.user_plan", user.plan)
            .apply()
        Log.i("SessionManager", "Datos espejados en FlutterSharedPreferences: id=${user.id}, email=${user.email}")
    }

    fun saveAdmin(isAdmin: Boolean) {
        prefs.edit()
            .putBoolean("is_admin", isAdmin)
            .apply()
        flutterPrefs.edit()
            .putBoolean("flutter.is_admin", isAdmin)
            .apply()
        Log.i("SessionManager", "Flag admin actualizado: $isAdmin")
    }

    fun clear() {
        prefs.edit().clear().apply()
        flutterPrefs.edit().clear().apply()
    }

    fun getId(): String? = prefs.getString("user_id", null)
    fun getEmail(): String? = prefs.getString("user_email", null)

    fun getCachedUser(): User? {
        val email = getEmail() ?: return null
        val id = getId() ?: ""
        val name = prefs.getString("user_name", "") ?: ""
        val plan = prefs.getString("user_plan", "Free") ?: "Free"
        Log.i("SessionManager", "Usuario recuperado del cache: id=$id, email=$email")
        return User(id = id, name = name, email = email, plan = plan)
    }

    fun isAdmin(): Boolean = prefs.getBoolean("is_admin", false)
}
