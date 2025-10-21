package mx.apb.beneficios_juventud.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.EstablecimientoItem
import mx.apb.beneficios_juventud.model.API.response.Categoria

data class MenuUiState(
    val categorias: List<Categoria> = emptyList(),
    val seleccionadas: Set<Int> = emptySet(),
    val search: String = "",
    val items: List<EstablecimientoItem> = emptyList(),
    val loading: Boolean = false
)

class MenuVM : ViewModel() {
    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state

    init {
        viewModelScope.launch {
            try {
                val resp = ClienteApi.service.obtenerCategorias()
                if (resp.success && resp.data != null) {
                    _state.update { it.copy(categorias = resp.data) }
                } else {
                    Log.w("API", "Categorias: success=false")
                }
            } catch (e: Exception) {
                Log.e("API", "Categorias error", e)
                // mostrar estado de error en UI
                _state.update { it.copy(categorias = emptyList()) }
            }
        }

        viewModelScope.launch {
            val resp = ClienteApi.service.obtenerCategorias()
            if (resp.success && resp.data != null) {
                _state.update { it.copy(categorias = resp.data) }
            }
        }

        viewModelScope.launch {
            _state
                .map { it.search to it.seleccionadas }
                .distinctUntilChanged()
                .debounce(350)
                .collect { (search, seleccionadas) ->
                    fetchEstablecimientos(search, seleccionadas)
                }
        }
    }

    fun onSearchChange(s: String) = _state.update { it.copy(search = s) }

    fun toggleCategoria(id: Int) = _state.update {
        val set = it.seleccionadas.toMutableSet()
        if (!set.add(id)) set.remove(id)
        it.copy(seleccionadas = set)
    }

    fun toggleTodas() = _state.update {
        val all = it.categorias.map { c -> c.idCategoria }.toSet()
        val nuevo = if (it.seleccionadas.size == all.size) emptySet() else all
        it.copy(seleccionadas = nuevo)
    }

    private suspend fun fetchEstablecimientos(search: String, seleccionadas: Set<Int>) {
        _state.update { it.copy(loading = true) }
        try {
            val categoryIds = if (seleccionadas.isEmpty()) null else seleccionadas.joinToString(",")
            val resp = ClienteApi.service.listarEstablecimientos(
                q = search.ifBlank { null }, categoryIds = categoryIds, page = 1, pageSize = 20
            )
            _state.update { it.copy(items = resp.data ?: emptyList(), loading = false) }
        } catch (e: Exception) {
            Log.e("API", "Establecimientos error", e)
            _state.update { it.copy(items = emptyList(), loading = false) }
        }
    }

}
