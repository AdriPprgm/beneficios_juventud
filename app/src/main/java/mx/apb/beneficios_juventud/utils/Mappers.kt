package mx.apb.beneficios_juventud.utils


import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.API.response.PerfilDto
import mx.apb.beneficios_juventud.model.API.response.PromocionNetwork
import mx.apb.beneficios_juventud.model.BeneficiarioDetalle
import mx.apb.beneficios_juventud.model.Promo
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


private fun buildNombre(
    nombreUsuario: String?,
    primerNombre: String?, segundoNombre: String?,
    apellidoPaterno: String?, apellidoMaterno: String?
): String {
    return when {
        !nombreUsuario.isNullOrBlank() -> nombreUsuario
        else -> listOfNotNull(
            primerNombre?.takeIf { it.isNotBlank() },
            segundoNombre?.takeIf { it.isNotBlank() },
            apellidoPaterno?.takeIf { it.isNotBlank() },
            apellidoMaterno?.takeIf { it.isNotBlank() }
        ).joinToString(" ").ifBlank { "Usuario" }
    }
}

fun PerfilDto.toBeneficiarioDetalle(): BeneficiarioDetalle =
    BeneficiarioDetalle(
        primerNombre, segundoNombre, apellidoPaterno, apellidoMaterno,
        fechaNacimiento, celular, folio, email, sexo
    )

// parsers “tolerantes” para calcular edad
private val formatosNacimiento = listOf(
    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    DateTimeFormatter.ISO_OFFSET_DATE_TIME,        // 2024-10-19T00:00:00Z
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
)

fun calcEdadFlexible(fecha: String?): Int {
    if (fecha.isNullOrBlank()) return 0
    for (f in formatosNacimiento) {
        try {
            val d = try { LocalDate.parse(fecha, f) }
            catch (_: DateTimeParseException) {
                // si vino con zona, conviértelo
                val odt = OffsetDateTime.parse(fecha, f)
                odt.toLocalDate()
            }
            return Period.between(d, LocalDate.now()).years
        } catch (_: Exception) { /* probar siguiente formato */ }
    }
    return 0
}

/** Mapea el DTO de la API al modelo que usa UI. */
fun PerfilDto.toPerfilUsuario(): PerfilUsuario =
    PerfilUsuario(
        name = buildNombre(nombreUsuario, primerNombre, segundoNombre, apellidoPaterno, apellidoMaterno),
        age = calcEdadFlexible(fechaNacimiento),
        photoUrl = null // TODO cuando la API exponga foto, CAMBIAR aquí.
    )

fun PromocionNetwork.toDomain() = Promo(
    id = idPromocion,
    titulo = titulo,
    descripcion = descripcion,
    imagenUrl = imagenURL,
    vigente = vigente,
    status = status,
    establecimientoNombre = establecimiento,
    establecimientoLogoUrl = establecimientoLogoURL,
    validFrom = validFrom,
    validTo = validTo,
    fechaRegistro = fechaRegistro
)
