package mx.apb.beneficios_juventud.model

data class Sucursal(
    val idSucursal: Long,
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val horaApertura: String?,
    val horaCierre: String?
)