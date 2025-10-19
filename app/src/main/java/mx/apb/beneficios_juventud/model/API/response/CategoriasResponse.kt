package mx.apb.beneficios_juventud.model.API.response

/**
 * Respuesta del endpoint GET /common/categorias
 * Requiere autenticación (roles: beneficiario, dueño, administrador)
 */
data class CategoriasResponse(
    val success: Boolean,
    val data: List<Categoria>?
)

/**
 * Representa una categoría dentro del sistema
 */
data class Categoria(
    val idCategoria: Int,
    val nombreCategoria: String
)
