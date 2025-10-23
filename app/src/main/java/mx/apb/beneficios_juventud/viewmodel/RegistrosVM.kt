package mx.apb.beneficios_juventud.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.response.RegistrosResponse

class RegistrosVM(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(EstadoRegistroCanje())
    val estado: StateFlow<EstadoRegistroCanje> = _estado

    // Ya no recibe parámetros
    suspend fun cargarRegistros() {
        try {
            val body: RegistrosResponse = ClienteApi.service.registroCanje()

            if (body.success) {
                val registros = body.data?.map { item ->
                    Registro(
                        nombreUsuario = item.nombreBeneficiario,
                        nombreOferta = item.tituloPromocion,
                        fechaHora = item.fechaAplicacion,
                        folio = item.folioBeneficiario
                    )
                } ?: emptyList()

                _estado.update { it.copy(registros = registros) }
                println("✅ Registros obtenidos: ${registros.size}")
            } else {
                println("⚠️ Error en la respuesta: ${body.message}")
            }

        } catch (e: Exception) {
            println("❌ Error al obtener registros: ${e.message}")
        }
    }
}

// Estado que expone los registros para el Composable
data class EstadoRegistroCanje(
    val registros: List<Registro> = emptyList()
)

// Modelo para usar en el Composable
data class Registro(
    val nombreUsuario: String,
    val nombreOferta: String,
    val fechaHora: String,
    val folio: String
)
