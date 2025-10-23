package mx.apb.beneficios_juventud.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.request.ScannerRequest
import mx.apb.beneficios_juventud.model.API.response.ScannerResponse

class ScannerVM(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(EstadoScanner())
    val estado: StateFlow<EstadoScanner> = _estado

    // Actualiza múltiples campos a la vez
    fun actualizarDatosQR(
        userId: String? = null,
        idPromocion: String? = null,
        timestamp: String? = null,
        expirationTime: String? = null,
        idDueno: String? = null
    ) {
        _estado.value = _estado.value.copy(
            userId = userId ?: _estado.value.userId,
            idPromocion = idPromocion ?: _estado.value.idPromocion,
            timestamp = timestamp ?: _estado.value.timestamp,
            expirationTime = expirationTime ?: _estado.value.expirationTime,
            idDueno = idDueno ?: _estado.value.idDueno

        )
    }


    // Llama al endpoint scanner
    suspend fun Scaneo() {
        val request = ScannerRequest(
            userId = _estado.value.userId,
            idPromocion = _estado.value.idPromocion,
            timestamp = _estado.value.timestamp,
            expirationTime = _estado.value.expirationTime,
            idDueno = _estado.value.idDueno
        )

        try {
            val response: retrofit2.Response<ScannerResponse> = ClienteApi.service.scanner(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    println("✅ Escaneo exitoso: ${body.message}")
                } else {
                    println("⚠️ Escaneo fallido: ${body?.message ?: "Body vacío"}")
                }
            } else {
                println("⚠️ Error HTTP: ${response.code()}")
            }

        } catch (e: Exception) {
            println("❌ Error al hacer la solicitud: ${e.message}")
        }
    }

}
