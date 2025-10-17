package mx.apb.beneficios_juventud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import mx.apb.beneficios_juventud.model.PerfilTemporal
import mx.apb.beneficios_juventud.view.Pantalla

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

class PerfilVM(
    private val repo: PerfilTemporal = PerfilTemporal()
) : ViewModel() {
    /** Modelo base de la aplicación, que contiene la lógica general de Beneficios Juventud. */
    private val modelo = BeneficiosJuventud()

    private val _uiState = MutableStateFlow<EstadoPerfil>(EstadoPerfil.Loading)
    val uiState: StateFlow<EstadoPerfil> = _uiState.asStateFlow()

    init {
        reload()
    }


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
            _uiState.value = try {
                EstadoPerfil.Exito(
                    perfil = repo.getPerfil(),
                    historial = repo.getHistorial()
                )
            } finally {
                _uiState.value = EstadoPerfil.Error("Error al cargar el perfil")
            }
        }
    }
}
