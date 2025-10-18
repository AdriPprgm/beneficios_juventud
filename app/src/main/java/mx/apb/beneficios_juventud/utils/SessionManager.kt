package mx.apb.beneficios_juventud.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * Gestor de sesión para manejar el token JWT y datos del usuario
 * @author Adrian Proaño Bernal
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("beneficios_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER = "user_data"
        private const val KEY_ROLE = "user_role"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FOLIO = "user_folio"
        private const val KEY_NOMBRE = "user_nombre"

        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Guarda el token JWT y los datos del usuario
     */
    fun saveSession(token: String, userSession: UserSession) {
        prefs.edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_USER, gson.toJson(userSession))
            putString(KEY_ROLE, userSession.role)
            putString(KEY_EMAIL, userSession.email)
            putInt(KEY_USER_ID, userSession.id)
            userSession.folio?.let { putString(KEY_FOLIO, it) }
            userSession.nombre?.let { putString(KEY_NOMBRE, it) }
            apply()
        }
    }

    /**
     * Obtiene el token JWT almacenado
     */
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    /**
     * Obtiene los datos del usuario almacenados
     */
    fun getUserSession(): UserSession? {
        val userJson = prefs.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(userJson, UserSession::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Obtiene el rol del usuario
     */
    fun getUserRole(): String? = prefs.getString(KEY_ROLE, null)

    /**
     * Obtiene el email del usuario
     */
    fun getUserEmail(): String? = prefs.getString(KEY_EMAIL, null)

    /**
     * Obtiene el ID del usuario
     */
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    /**
     * Obtiene el folio del beneficiario (si aplica)
     */
    fun getUserFolio(): String? = prefs.getString(KEY_FOLIO, null)

    /**
     * Verifica si hay una sesión activa
     */
    fun isLoggedIn(): Boolean = getToken() != null

    /**
     * Verifica si el usuario es administrador
     */
    fun isAdmin(): Boolean = getUserRole() == "administrador"

    /**
     * Verifica si el usuario es dueño
     */
    fun isDueno(): Boolean = getUserRole() == "dueno"

    /**
     * Verifica si el usuario es beneficiario
     */
    fun isBeneficiario(): Boolean = getUserRole() == "beneficiario"

    /**
     * Limpia la sesión (logout)
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    /**
     * Actualiza solo el token (para refresh token futuro)
     */
    fun updateToken(newToken: String) {
        prefs.edit().putString(KEY_TOKEN, newToken).apply()
    }
}

/**
 * Modelo para la sesión del usuario
 */
data class UserSession(
    val id: Int,
    val email: String,
    val role: String,
    val nombre: String? = null,
    val nombreUsuario: String? = null,
    val folio: String? = null
)