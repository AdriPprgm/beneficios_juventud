package mx.apb.beneficios_juventud.model.API.response

/**
 * Respuesta del endpoint /mobile/perfil
 * Nota: la API devuelve "data" como un arreglo (aunque sea 1 elemento).
 */
data class PerfilResponse(
    val success: Boolean,
    val data: List<PerfilDto>
)

/**
 * DTO "superset" que cubre los 3 roles:
 * - beneficiario: nombres, apellidos, fechaNacimiento, etc.
 * - dueno/admin: nombreUsuario, email
 */
data class PerfilDto(
    // Beneficiario
    val primerNombre: String? = null,
    val segundoNombre: String? = null,
    val apellidoPaterno: String? = null,
    val apellidoMaterno: String? = null,
    val fechaNacimiento: String? = null, // esperado: yyyy-MM-dd
    val celular: String? = null,
    val folio: String? = null,
    val email: String? = null,
    val sexo: String? = null,

    // Dueño/Admin
    val nombreUsuario: String? = null
)
