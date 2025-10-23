package mx.apb.beneficios_juventud.model.API.request


data class AgrgarOfertaRequest (
    val titulo: String,
    val descripcion: String,
    val discountType: String,
    val discountValue: Double,
    val validFrom: String,
    val validTo: String
)