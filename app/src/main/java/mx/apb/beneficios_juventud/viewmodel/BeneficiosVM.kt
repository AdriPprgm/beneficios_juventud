package mx.apb.beneficios_juventud.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import mx.apb.beneficios_juventud.model.ClienteApi
import mx.apb.beneficios_juventud.model.LoginRequest

/**
 * Un Viewmodel de la arquitectura
 */

class BeneficiosVM : ViewModel()
{
    // Modelo
    private val modelo = BeneficiosJuventud()

    // Estado de la Aplicación
    private val _estado = MutableStateFlow(EstadoBeneficios())
    val estado: StateFlow<EstadoBeneficios> = _estado // No usamos mutable, porque esta variable es pública
    // estado no altera la variable, _estado sí puede alterarlar, pero como es privada solo se puede ver


    // Interfaz de login
    fun actualizarCredencial(credencialIngresada: String) {
        _estado.value = _estado.value.copy(credencial = credencialIngresada)
    }
    fun actualizarContrasena(contrasenaIngresada: String) {
        _estado.value = _estado.value.copy(contrasena = contrasenaIngresada)
    }
    fun obtenerContrasena(): String {
        return _estado.value.contrasena
    }
    fun obtenerCredencial(): String {
        return _estado.value.credencial
    }

    fun borrarDatos() {
        actualizarCredencial("")
        actualizarContrasena("")
    }


    // Interfaz para la vista de mapa
    fun actualizarSolicitudMapa(solicitudIngresada: String) {
        _estado.value = _estado.value.copy(solicitudMapa = solicitudIngresada)
    }

    suspend fun Login() {
        val request = LoginRequest.create(
            rawCredencial = obtenerCredencial(),
            rawContrasena = obtenerContrasena()
        )
        try {
            val response = ClienteApi.service.login(request)
            Log.d("API_TEST", "Success: ${response.success}, Message: ${response.message}")
            _estado.value = _estado.value.copy(loginSuccess = response.success)
        } catch (e: Exception) {
            Log.e("API_TEST", "Error: ${e.message}", e)
            _estado.value = _estado.value.copy(loginSuccess = false)
        }
    }

    suspend fun LogOut(){

    }
}