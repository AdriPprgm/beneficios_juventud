package mx.apb.beneficios_juventud.model

/**
 * Modelo de datos que representa una notificación dentro del sistema
 * **Beneficios Juventud**.
 *
 * Esta clase se utiliza para mostrar avisos, recordatorios o mensajes informativos
 * al usuario, generalmente asociados a promociones, eventos o actividades del programa.
 *
 * Cada notificación contiene un título breve, una descripción detallada,
 * una fecha de emisión y un distintivo visual (*badge*) que puede indicar
 * su tipo o prioridad (por ejemplo, “Nuevo”, “Importante”, “Expira pronto”).
 */

data class Notificacion(
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val badge: String
)