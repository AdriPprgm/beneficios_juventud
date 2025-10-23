package mx.apb.beneficios_juventud.model.API.response

data class NotificacionesResponse(
    val success: Boolean,
    val notifications: List<NotificacionRemota>?
)

data class NotificacionRemota(
    val titulo: String,
    val descripcion: String,
    val validTo: String
)
