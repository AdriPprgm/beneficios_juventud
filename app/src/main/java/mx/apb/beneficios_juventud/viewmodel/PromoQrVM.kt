package mx.apb.beneficios_juventud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.ServicioApi
import mx.apb.beneficios_juventud.model.API.response.QrData

data class PromoQrUiState(
    val loading: Boolean = false,
    val qr: QrData? = null,
    val error: String? = null,
    val remainingSec: Long = 0L
)

class PromoQrVM(
    private val api: ServicioApi,
    private val tokenProvider: () -> String
) : ViewModel() {

    private val _state = MutableStateFlow(PromoQrUiState())
    val state: StateFlow<PromoQrUiState> = _state

    private var tickerJob: Job? = null

    fun cargarQr(idPromocion: Long) {
        tickerJob?.cancel()
        _state.value = PromoQrUiState(loading = true)

        viewModelScope.launch {
            try {
                val bearer = "Bearer ${tokenProvider()}"
                val resp = api.generarQr(idPromocion, bearer)
                if (!resp.success || resp.data == null) {
                    _state.value = PromoQrUiState(error = resp.message ?: "Error al generar QR")
                    return@launch
                }
                val nowSec = System.currentTimeMillis() / 1000
                val remaining: Long = (resp.data.expiresAtEpochSec - nowSec).coerceAtLeast(0L)


                _state.value = PromoQrUiState(
                    loading = false,
                    qr = resp.data,
                    error = null,
                    remainingSec = remaining
                )
                iniciarTicker()
            } catch (e: Exception) {
                _state.value = PromoQrUiState(error = e.message ?: "Error de red")
            }
        }
    }

    fun regenerar(idPromocion: Long) = cargarQr(idPromocion)

    private fun iniciarTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val s = _state.value
                if (s.qr == null) break
                val nowSec = System.currentTimeMillis() / 1000
                val remaining = (s.qr.expiresAtEpochSec - nowSec).coerceAtLeast(0)
                _state.value = s.copy(remainingSec = remaining)
                if (remaining == 0L) break
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }

    companion object {
        /**
         * Factory embebido para usar con `viewModel(factory = PromoQrVM.factory(api, tokenProvider))`
         */
        fun factoryQr(
            api: ServicioApi,
            tokenProvider: () -> String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(PromoQrVM::class.java)) {
                    "PromoQrVM.factory: modelClass inválido"
                }
                return PromoQrVM(api, tokenProvider) as T
            }
        }
    }
}
