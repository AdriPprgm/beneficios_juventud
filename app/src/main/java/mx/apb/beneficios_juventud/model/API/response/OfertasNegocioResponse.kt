package mx.apb.beneficios_juventud.model.API.response

import com.google.gson.annotations.SerializedName
import mx.apb.beneficios_juventud.model.OfertaNegocio

data class OfertasNegocioResponse(
    @SerializedName("success")
    val success: Boolean,

    // Maps the "items" array in the JSON to the "ofertas" list in this class
    @SerializedName("items")
    val ofertas: List<OfertaNegocio>?,

    // The "message" field is not in your JSON, so we make it nullable and optional
    @SerializedName("message")
    val message: String?,

    // Add the "total" field from the JSON, as it can be useful
    @SerializedName("total")
    val total: Int?
)
