package mx.apb.beneficios_juventud.model.API.response

/**
 * Modelo de respuesta del endpoint de login.
 *
 * Coincide exactamente con la respuesta del backend:
 * {
 *   "success": true,
 *   "message": "Login exitoso",
 *   "token": "eyJhbGciOiJIUzI1NiIs...",
 *   "user": {
 *     "id": 1,
 *     "email": "usuario@ejemplo.com",
 *     "role": "beneficiario",
 *     "nombre": "Juan González",
 *     "folio": "BJ12345678"
 *   }
 * }
 */
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserData? = null
)

/**
 * Información del usuario autenticado.
 */
data class UserData(
    val id: Int,
    val email: String,
    val role: String,
    val nombre: String? = null,
    val folio: String? = null
)