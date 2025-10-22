package mx.apb.beneficios_juventud.model.API.response

import mx.apb.beneficios_juventud.model.Sucursal

/**
 * Respuesta del endpoint /mobile/ubicacion-sucursales.
 * Estructura típica:
 * {
 *   "success": true,
 *   "data": [ { idSucursal, nombre, latitud, longitud, horaApertura, horaCierre } ],
 *   "message": null
 * }
 */
data class UbicacionResponse(
    val success: Boolean,
    val data: List<Sucursal>?,
    val message: String?
)
