package mx.apb.beneficios_juventud.model.API.request

/**
 * Modelo de solicitud para el endpoint de login.
 *
 * IMPORTANTE:
 * La contraseña se envía en texto plano.
 * La seguridad está garantizada por HTTPS y bcrypt en el backend.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
