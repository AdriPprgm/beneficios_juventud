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
    val imagenRes: Int,
    val titulo: String,
    val descripcion: String,
    val badge: String
)