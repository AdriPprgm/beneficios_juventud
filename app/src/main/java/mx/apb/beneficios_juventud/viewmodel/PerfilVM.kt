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

class PerfilVM(
    private val repo: PerfilTemporal = PerfilTemporal()
) : ViewModel() {
    private val modelo = BeneficiosJuventud()

    private val _uiState = MutableStateFlow<EstadoPerfil>(EstadoPerfil.Loading)
    val uiState: StateFlow<EstadoPerfil> = _uiState.asStateFlow()

    init {
        reload()
    }

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
