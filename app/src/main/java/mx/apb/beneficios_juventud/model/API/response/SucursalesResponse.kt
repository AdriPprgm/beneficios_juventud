package mx.apb.beneficios_juventud.model.API.response

data class SucursalDTO(
    val idSucursal: Int,
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val horaApertura: String?, // o LocalTime si lo parseas
    val horaCierre: String?
)

data class SucursalesResponse(
    val success: Boolean,
    val data: List<SucursalDTO>?
)
