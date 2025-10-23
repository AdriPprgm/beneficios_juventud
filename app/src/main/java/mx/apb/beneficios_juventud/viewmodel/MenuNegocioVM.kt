package mx.apb.beneficios_juventud.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.request.AgrgarOfertaRequest
import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import mx.apb.beneficios_juventud.model.OfertaNegocio

data class NegocioUIEstado(
    val idDueno: Int? = null,
    val ofertas: List<OfertaNegocio> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class NegocioVM() : ViewModel(){
    private val _estado = MutableStateFlow(NegocioUIEstado())
    val estado: StateFlow<NegocioUIEstado> = _estado

    fun setIdDuenoYRecargar(id: Int?) {
        if (id == null) {
            _estado.value = _estado.value.copy(error = "ID de dueño no válido.")
            return
        }
        _estado.value = _estado.value.copy(idDueno = id)
        obtenerOfertas()
    }

    private fun obtenerOfertas() {
        val idDueno = _estado.value.idDueno
        if (idDueno == null) {
            Log.e("NegocioVM", "No se puede obtener ofertas porque el idDueno es nulo.")
            _estado.value = _estado.value.copy(error = "Falta el ID del dueño.")
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(loading = true)
            try {
                Log.d("NegocioVM", "Buscando ofertas para el dueño con ID: $idDueno")
                val response = ClienteApi.service.ofertasNegocio(idDueno)
                if (response.success && response.ofertas != null) {
                    _estado.value = _estado.value.copy(
                        ofertas = response.ofertas,
                        loading = false,
                        error = null // Clear previous errors
                    )
                } else {
                    val errorMessage = response.message ?: "La API devolvió un error inesperado."
                    Log.e("NegocioVM", "Error al cargar ofertas: $errorMessage")
                    _estado.value = _estado.value.copy(
                        loading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("NegocioVM", "Excepción al cargar ofertas: ${e.message}", e)
                _estado.value = _estado.value.copy(
                    loading = false,
                    error = "No se pudieron cargar las ofertas. Revisa tu conexión."
                )
            }
        }
    }

    fun agregarOferta(request: AgrgarOfertaRequest) {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(loading = true)
            try {
                val response = ClienteApi.service.publicarOferta(request)
                if (response.success) {
                    Log.d("NegocioVM", "Oferta agregada con éxito: ${response.message}")
                    obtenerOfertas()
                } else {
                    val errorMessage = response.message ?: "La API devolvió un error al agregar la oferta."
                    Log.e("NegocioVM", "Error al agregar oferta: $errorMessage")
                    _estado.value = _estado.value.copy(
                        loading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("NegocioVM", "Excepción al agregar oferta: ${e.message}", e)
                _estado.value = _estado.value.copy(
                    loading = false,
                    error = "No se pudo agregar la oferta. Revisa tu conexión."
                )
            }
        }
    }

    fun eliminarOferta(idOferta: Int) {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(loading = true)
            try {
                val response = ClienteApi.service.eliminarOferta(idOferta)
                if (response.success) {
                    Log.d("NegocioVM", "Oferta eliminada con éxito: ${response.message}")
                    obtenerOfertas()
                } else {
                    val errorMessage = response.message ?: "La API devolvió un error al eliminar la oferta."
                    Log.e("NegocioVM", "Error al eliminar oferta: $errorMessage")
                    _estado.value = _estado.value.copy(
                        loading = false,
                        error = errorMessage
                    )
                }
            } catch (e: Exception) {
                Log.e("NegocioVM", "Excepción al eliminar oferta: ${e.message}", e)
            }
        }
    }
}