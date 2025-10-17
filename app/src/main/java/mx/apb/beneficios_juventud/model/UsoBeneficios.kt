package mx.apb.beneficios_juventud.model

import java.time.LocalDate

/**
 * Modelo de datos que representa el registro del uso de un beneficio
 * dentro del sistema **Beneficios Juventud**.
 *
 * Esta clase almacena la información relacionada con una transacción o evento
 * en el que un usuario aprovecha un beneficio (por ejemplo, un descuento o promoción)
 * en un establecimiento participante.
 *
 * Se utiliza comúnmente en el historial del perfil del usuario para mostrar
 * los beneficios que ha utilizado, junto con la fecha, la descripción y la categoría.
 */

data class UsoBeneficios(
    val id: String,
    val merchant: String,
    val date: LocalDate,
    val description: String,
    val category: CategoriaBeneficios
)
