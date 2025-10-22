package mx.apb.beneficios_juventud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.API.ServicioApi
import mx.apb.beneficios_juventud.model.Promo
import mx.apb.beneficios_juventud.utils.toDomain

/**
 * Representa el estado UI de la lista de promociones asociadas a un establecimiento.
 *
 * @property cargando indica si los datos se están obteniendo de la API.
 * @property error contiene el mensaje de error en caso de que la carga falle.
 * @property page número de página actual para la paginación.
 * @property pageSize cantidad de elementos mostrados por página.
 * @property total número total de promociones disponibles.
 * @property items lista de promociones obtenidas desde la API.
 */
data class PromosUiState(
    val cargando: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val pageSize: Int = 20,
    val total: Int = 0,
    val items: List<Promo> = emptyList()
)

/**
 * ViewModel encargado de gestionar el estado y la lógica de negocio
 * del detalle de un establecimiento, incluyendo la carga de sus promociones.
 *
 * Se comunica con la capa de red a través de [ServicioApi] y expone
 * los resultados mediante un flujo reactivo de tipo [StateFlow].
 *
 * @property api instancia de [ServicioApi] utilizada para realizar peticiones a la API REST.
 */
class DetalleEstablecimientoVM(
    private val api: ServicioApi
) : ViewModel() {

    /** Flujo mutable que mantiene el estado actual de la interfaz de promociones. */
    private val _promos = MutableStateFlow(PromosUiState())

    /** Flujo público inmutable que expone el estado de las promociones a la UI. */
    val promos: StateFlow<PromosUiState> = _promos

    /**
     * Carga las promociones disponibles para un establecimiento específico.
     *
     * Este método realiza la petición a la API, actualiza el estado de carga
     * y gestiona tanto los resultados exitosos como los errores.
     *
     * @param idEstablecimiento identificador único del establecimiento.
     * @param page número de página actual (por defecto 1).
     * @param pageSize cantidad de elementos por página (por defecto 20).
     */
    fun cargarPromociones(idEstablecimiento: Int, page: Int = 1, pageSize: Int = 20) {
        viewModelScope.launch {
            _promos.update { it.copy(cargando = true, error = null) }

            runCatching {
                api.promocionesPorEstablecimiento(idEstablecimiento, page, pageSize)
            }.onSuccess { resp ->
                _promos.value = PromosUiState(
                    cargando = false,
                    error = null,
                    page = resp.page,
                    pageSize = resp.pageSize,
                    total = resp.total,
                    items = resp.items.map { it.toDomain() }
                )
            }.onFailure { e ->
                _promos.update { it.copy(cargando = false, error = e.message ?: "Error") }
            }
        }
    }

    /**
     * Fábrica para crear instancias de [DetalleEstablecimientoVM]
     * con la inyección explícita del servicio API necesario.
     *
     * Se utiliza al invocar `viewModel()` desde una función composable
     * para proveer la dependencia [ServicioApi].
     *
     */
    companion object {
        /**
         * Crea un [ViewModelProvider.Factory] que devuelve una instancia de
         * [DetalleEstablecimientoVM] con la dependencia [ServicioApi] inyectada.
         *
         * @param api instancia del cliente Retrofit para acceder a la API.
         * @return una fábrica lista para usarse en `viewModel(factory = ...)`.
         */
        fun factory(api: ServicioApi): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetalleEstablecimientoVM(api) as T
                }
            }
    }
}




