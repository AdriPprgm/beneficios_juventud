package mx.apb.beneficios_juventud.model

/**
 * Modelo de datos que representa una promoción dentro del sistema
 * **Beneficios Juventud**.
 *
 * Esta clase se utiliza para mostrar ofertas o descuentos disponibles
 * en los distintos establecimientos afiliados al programa.
 *
 * Cada promoción contiene un recurso de imagen local, un título,
 * una descripción y un distintivo (*badge*) que permite identificar
 * su tipo o relevancia.
 */

data class Promo(
    val id: Int,
    val titulo: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val vigente: Boolean,
    val status: String?,
    val establecimientoNombre: String,
    val establecimientoLogoUrl: String?,
    val validFrom: String?,
    val validTo: String?,
    val fechaRegistro: String?
)