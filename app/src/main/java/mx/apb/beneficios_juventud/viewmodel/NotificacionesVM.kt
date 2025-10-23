package mx.apb.beneficios_juventud.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.response.NotificacionesResponse

class NotificacionesVM(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(EstadoNotificaciones())
    val estado: StateFlow<EstadoNotificaciones> = _estado

    suspend fun cargarNotificaciones() {
        try {
            val body: NotificacionesResponse = ClienteApi.service.obtenerNotificaciones()
            if (body.success) {
                val notifs = body.notifications?.map {
                    NotificacionUI(
                        titulo = it.titulo,
                        descripcion = it.descripcion,
                        fecha = it.validTo
                    )
                } ?: emptyList()

                _estado.update { it.copy(notificaciones = notifs) }
                println("✅ Notificaciones obtenidas: ${notifs.size}")
            } else {
                println("⚠️ Error en respuesta: ${body}")
            }
        } catch (e: Exception) {
            println("❌ Error al cargar notificaciones: ${e.message}")
        }
    }
}

data class EstadoNotificaciones(
    val notificaciones: List<NotificacionUI> = emptyList()
)

data class NotificacionUI(
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val badge: String = ""
)
