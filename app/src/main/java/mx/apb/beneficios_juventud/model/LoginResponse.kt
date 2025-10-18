package mx.apb.beneficios_juventud.model


/**
 * Representa la respuesta del servidor al realizar una solicitud de inicio de sesión
 * dentro del sistema **Beneficios Juventud**.
 *
 * Esta clase es utilizada por Retrofit para mapear automáticamente
 * los datos JSON recibidos desde la API a un objeto Kotlin.
 */
data class LoginResponse(
    val success: Boolean,
    val message: String
)
