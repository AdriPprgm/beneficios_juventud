package mx.apb.beneficios_juventud.model.API.response

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    val success: Boolean,
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val items: List<T>
)

data class PromocionNetwork(
    val idPromocion: Int,
    val idEstablecimiento: Int,
    val establecimiento: String,
    val establecimientoLogoURL: String?,
    val titulo: String,
    val descripcion: String?,
    val imagenURL: String?,
    val validFrom: String?,
    val validTo: String?,
    @SerializedName("esVigente") val esVigenteInt: Int?,
    val status: String?,
    val fechaRegistro: String?
) {
    val vigente: Boolean get() = esVigenteInt == 1
}
