package mx.apb.beneficios_juventud.model

import com.google.gson.annotations.SerializedName

/**
 * @author: Israel González Huerta
 * Representa una oferta creada por un negocio.
 *
 * @property id Identificador único de la oferta.
 * @property imagenRes Recurso de imagen asociado a la oferta.
 * @property titulo Título o nombre de la oferta.
 * @property descripcion Descripción detallada de la oferta.
 */
data class OfertaNegocio(
    // Maps the "idPromocion" JSON field to the "id" Kotlin property
    @SerializedName("idPromocion")
    val id: Int,

    // "titulo" and "descripcion" already match, but adding the annotation is good practice
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    // Maps "imagenURL" to "imagenURL". This is a String and can be null.
    @SerializedName("imagenURL")
    val imagenURL: String?,

    // Add this field from the JSON
    @SerializedName("establecimiento")
    val establecimiento: String
)