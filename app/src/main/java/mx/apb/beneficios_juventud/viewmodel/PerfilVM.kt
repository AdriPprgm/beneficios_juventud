package mx.apb.beneficios_juventud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.ServicioApi
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.UsoBeneficios
import mx.apb.beneficios_juventud.model.CategoriaBeneficios
import mx.apb.beneficios_juventud.utils.toBeneficiarioDetalle
import mx.apb.beneficios_juventud.utils.toPerfilUsuario
import java.time.LocalDate

/**
 * ViewModel encargado de gestionar el estado de la vista de perfil del usuario.
 *
 * Esta clase actúa como intermediario entre la capa de vista (UI) y la capa de datos,
 * manteniendo el estado del perfil del usuario y su historial de uso.
 * Utiliza corrutinas para cargar los datos de manera asíncrona y exponerlos a la interfaz
 * mediante un flujo de estado reactivo (`StateFlow`).
 *
 * @property repo Repositorio temporal que proporciona los datos del perfil y el historial del usuario.
 * @constructor Crea una instancia de [PerfilVM] con un repositorio por defecto de tipo [PerfilTemporal].
 */

class PerfilVM : ViewModel() {
    private val api: ServicioApi = ClienteApi.service

    private val _uiState = MutableStateFlow<EstadoPerfil>(EstadoPerfil.Loading)
    val uiState: StateFlow<EstadoPerfil> = _uiState.asStateFlow()

    init { reload() }

    /**
     * Recarga los datos del perfil del usuario y su historial.
     *
     * Se ejecuta dentro de una corrutina del [viewModelScope], por lo que no bloquea el hilo principal.
     * Cambia el estado a [EstadoPerfil.Loading] mientras se realiza la operación, y luego
     * emite [EstadoPerfil.Exito] si la carga fue exitosa o [EstadoPerfil.Error] si ocurre algún fallo.
     */
    fun reload() {
        viewModelScope.launch {
            _uiState.value = EstadoPerfil.Loading
            try {
                val res = api.getPerfil()
                if (!res.success) {
                    _uiState.value = EstadoPerfil.Error("La API respondió success=false")
                    return@launch
                }

                val dto = res.data.firstOrNull()
                    ?: run {
                        _uiState.value = EstadoPerfil.Error("Perfil vacío")
                        return@launch
                    }

                val perfil = dto.toPerfilUsuario()
                val detalle = dto.toBeneficiarioDetalle()
                val historial = emptyList<mx.apb.beneficios_juventud.model.UsoBeneficios>()

                _uiState.value = EstadoPerfil.Exito(
                    perfil = perfil,
                    historial = historial,
                    detalle = detalle
                )

            } catch (e: Exception) {
                _uiState.value = EstadoPerfil.Error(e.message ?: "Error al cargar el perfil")
            }
        }
    }
}




