package mx.apb.beneficios_juventud.model.API.response

data class FolioResponse(
    val success: Boolean,
    val data: UsuarioData?,
    val message: String?
)

data class UsuarioData(
    val nombre: String,
    val edad: Int
)
