package pe.edu.upc.certiweb_mobile_application.config

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHash {
    fun salt(length: Int = 16): String {
        val rnd = SecureRandom()
        val bytes = ByteArray(length)
        rnd.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    fun hash(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val input = (salt + password).toByteArray(Charsets.UTF_8)
        val digest = md.digest(input)
        return digest.joinToString(separator = "") { b -> "%02x".format(b) }
    }
}