package mx.apb.beneficios_juventud.model

import com.google.android.gms.maps.model.LatLng

/**
 * Modelo listo para pintar en el mapa.
 */
data class PinMapa(
    val id: Long,
    val titulo: String,
    val snippet: String,
    val pos: LatLng
)