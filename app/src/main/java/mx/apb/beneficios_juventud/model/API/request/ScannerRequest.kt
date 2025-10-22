package mx.apb.beneficios_juventud.model.API.request

data class ScannerRequest(
    val userId: String,
    val idPromocion: String,
    val timestamp: String,
    val expirationTime: String
)
