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
        return _estado.value.contrasena
    }

}