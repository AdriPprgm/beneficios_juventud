package mx.apb.beneficios_juventud.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.apb.beneficios_juventud.model.BeneficiosJuventud

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
    fun actualizarContrasena(contrasenaIngresada: String) {
        _estado.value = _estado.value.copy(contrasena = contrasenaIngresada)
    }
    fun actualizarCorreo(correoIngresado: String) {
        _estado.value = _estado.value.copy(correo = correoIngresado)
    }
    fun actualizarCelular(celularIngresado: String) {
        _estado.value = _estado.value.copy(contrasena = celularIngresado)
    }
    fun obtenerContrasena(): String {
        return _estado.value.contrasena
    }
    fun obtenerCorreo(): String {
        return _estado.value.contrasena
    }

}