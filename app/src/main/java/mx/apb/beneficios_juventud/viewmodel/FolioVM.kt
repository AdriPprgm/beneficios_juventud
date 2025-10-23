package mx.apb.beneficios_juventud.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import mx.apb.beneficios_juventud.model.API.ClienteApi

class FolioVM(application: Application) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(EstadoFolio())
    val estado: StateFlow<EstadoFolio> = _estado

    suspend fun buscarFolio(folio: String) {
        try {
            val response = ClienteApi.service.validarFolio(folio)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) { // 👈 aquí ya compila
                    println("✅ Folio válido: ${body.data?.nombre}, ${body.data?.edad} años")
                    _estado.update {
                        it.copy(
                            nombre = body.data?.nombre ?: "",
                            edad = body.data?.edad?.toString() ?: ""
                        )

                    }
                } else {
                    println("⚠️ Folio inválido o sin datos: ${body?.message}")
                }
            } else {
                println("⚠️ Error HTTP: ${response.code()}")
            }
        } catch (e: Exception) {
            println("❌ Error al consultar folio: ${e.message}")
        }
    }
}

data class EstadoFolio(
    val nombre: String = "",
    val edad: String = ""
)
