package mx.apb.beneficios_juventud.model.API.response

data class RegistrosResponse(
    val success: Boolean,
    val data: List<RegistroCanjeItem>?,
    val message: String?
)

data class RegistroCanjeItem(
    val nombreBeneficiario: String,
    val folioBeneficiario: String,
    val tituloPromocion: String,
    val fechaAplicacion: String
)
