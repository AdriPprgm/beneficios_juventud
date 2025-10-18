package mx.apb.beneficios_juventud.model

import androidx.compose.runtime.Immutable

/**
 * Modelo de datos que representa la información básica de un usuario
 * dentro del sistema **Beneficios Juventud**.
 *
 * Esta clase define los atributos principales asociados al perfil del usuario,
 * como su nombre, edad y una posible URL de fotografía.
 *
 * Está marcada con la anotación [Immutable], lo que indica que sus propiedades
 * no deben cambiar después de ser creadas. Esto mejora el rendimiento en
 * entornos **Jetpack Compose**, ya que evita recomposiciones innecesarias
 * al detectar que el objeto no puede mutar.
 */

@Immutable
data class PerfilUsuario(
    val name: String,
    val age: Int,
    val photoUrl: String? = null
)