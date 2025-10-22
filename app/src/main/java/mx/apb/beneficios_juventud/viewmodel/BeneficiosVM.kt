package mx.apb.beneficios_juventud.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.API.request.LoginRequest
import mx.apb.beneficios_juventud.model.API.response.UbicacionResponse
import mx.apb.beneficios_juventud.model.ManagerSesion
import mx.apb.beneficios_juventud.model.PinMapa
import mx.apb.beneficios_juventud.model.Sucursal

/**
 * @author: Israel González Huerta
 * @author: Adrián Proaño Bernal
 * @author: Juan Pablo Solís Gómez
 *
 * ViewModel principal de la aplicación Beneficios Juventud.
 *
 * Maneja el estado de la sesión de usuario, credenciales, login/logout y datos relacionados
 * con la interfaz de usuario, incluyendo solicitudes para el mapa y almacenamiento temporal.
 */
class BeneficiosVM(application: Application) : AndroidViewModel(application) {

    private val managerSesion = ManagerSesion(application)

    /** Modelo que simula o contiene datos del usuario. */
    private val modelo = BeneficiosJuventud()

    /** Estado mutable interno de la aplicación. */
    private val _estado = MutableStateFlow(EstadoBeneficios())

    /** Estado público inmutable expuesto a la UI. */
    val estado: StateFlow<EstadoBeneficios> = _estado

    init {
        verificarSesion()
        ClienteApi.setSessExpiredCallback {
            handleSessExpired()
        }
    }

    private fun handleSessExpired(){
        viewModelScope.launch {
            // Actualiza el estado para notificar a la UI
            _estado.value = _estado.value.copy(expiredSess = true)
        }
    }

    fun onSessionExpiredDialogDismissed() {
        signOut() // Cierra la sesión completamente
        _estado.value = _estado.value.copy(expiredSess = false)
    }

    /**
     * Revisa si existe un token de sesión guardado al iniciar la app.
     * Si existe, restaura el estado de login.
     * @author Adrián Proaño Bernal
     */
    private fun verificarSesion() {
        viewModelScope.launch {
            val token = managerSesion.authToken.first() // Obtiene el valor actual del Flow
            if (!token.isNullOrBlank()) {
                Log.d("SESSION_MGR", "Token encontrado. Restaurando sesión.")
                ClienteApi.actualizarToken(token) // Prepara el token para futuras llamadas a la API
                _estado.value = _estado.value.copy(loginSuccess = true)
            } else {
                Log.d("SESSION_MGR", "Token no encontrado. No hay sesión activa.")
            }
            _estado.value = _estado.value.copy(verifyingSess = false)
        }
    }

    // ---------------------
    // Funciones de login
    // ---------------------

    /** Actualiza la credencial ingresada por el usuario (correo o teléfono). */
    fun actualizarCredencial(credencialIngresada: String) {
        _estado.value = _estado.value.copy(credencial = credencialIngresada)
    }

    /** Actualiza la contraseña ingresada por el usuario. */
    fun actualizarContrasena(contrasenaIngresada: String) {
        _estado.value = _estado.value.copy(contrasena = contrasenaIngresada)
    }

    /** Retorna la contraseña actualmente almacenada en el estado. */
    fun obtenerContrasena(): String = _estado.value.contrasena

    /** Retorna la credencial actualmente almacenada en el estado. */
    fun obtenerCredencial(): String = _estado.value.credencial

    /**
     * Actualiza el estado de login.
     * @param variableInsana Boolean que indica si el usuario está loggeado.
     */
    fun actualizarEstaLoggeado(variableInsana: Boolean) {
        _estado.value = _estado.value.copy(loginSuccess = variableInsana)
    }

    /** Borra los datos de login (credencial y contraseña). */
    fun borrarDatos() {
        actualizarCredencial("")
        actualizarContrasena("")
    }

    // ---------------------
    // Funciones de mapa
    // ---------------------

    /**
     * Actualiza la solicitud del mapa que ingresa el usuario.
     * @param solicitudIngresada Texto con la solicitud de ubicación.
     */
    fun actualizarSolicitudMapa(solicitudIngresada: String) {
        _estado.value = _estado.value.copy(solicitudMapa = solicitudIngresada)
    }

    /**
     * Carga sucursales desde la API /mobile/ubicacion-sucursales
     * y mapea a pines para el GoogleMap.
     */
    fun cargarSucursales() {
        viewModelScope.launch {
            _estado.update { it.copy(cargandoMapa = true, errorMapa = null) }
            try {
                val resp: UbicacionResponse = ClienteApi.service.ubicacionSucursales()
                val pins = if (resp.success && resp.data != null) {
                    resp.data.toPins()
                } else {
                    emptyList()
                }
                _estado.update { it.copy(pinsMapa = pins, cargandoMapa = false) }
            } catch (e: Exception) {
                Log.e("MAPA_API", "Error al cargar sucursales: ${e.message}", e)
                _estado.update { it.copy(cargandoMapa = false, errorMapa = e.message ?: "Error") }
            }
        }
    }

    /** Mapper: del DTO de sucursal a modelo de pin para mapa. */
    private fun List<Sucursal>.toPins(): List<PinMapa> =
        map { s ->
            PinMapa(
                id = s.idSucursal,
                titulo = s.nombre,
                snippet = "Abre: ${s.horaApertura?.take(5) ?: "—"} • Cierra: ${s.horaCierre?.take(5) ?: "—"}",
                pos = LatLng(s.latitud, s.longitud)
            )
        }

    // ---------------------
    // Función de autenticación
    // ---------------------

    /**
     * Realiza el login del usuario contra la API.
     *
     * Usa las credenciales almacenadas en el estado y actualiza `loginSuccess` según
     * el resultado de la API. Si la autenticación es exitosa, almacena el correo, token, rol, nombre y folio en el modelo.
     */
    suspend fun Login() {
        val request = LoginRequest(
            email = obtenerCredencial(),
            password = obtenerContrasena()
        )

        _estado.value = _estado.value.copy(LoadingLogin = true)

        try {
            val response = ClienteApi.service.login(request)
            Log.d("API_TEST", "Success: ${response.success}, Message: ${response.message}")

            if (response.success) {
                // Guardar datos del usuario en el modelo
                modelo.setCorreo(response.user?.email ?: "")
                modelo.setToken(response.token)
                modelo.setRol(response.user?.role)
                modelo.setNombre(response.user?.nombre)
                modelo.setFolio(response.user?.folio)

                // Actualizar el token global del ClienteApi (para el interceptor)
                response.token?.let { managerSesion.saveAuthToken(it) }
                ClienteApi.actualizarToken(response.token)

                // Logs de depuración
                Log.d(
                    "USER_SESSION", """
                    Sesión iniciada correctamente
                    Correo: ${modelo.correo}
                    Rol: ${modelo.rol}
                    Token: ${modelo.token?.take(40)}... (truncado)
                """.trimIndent()
                )

                // Actualizar estado de login
                _estado.value = _estado.value.copy(loginSuccess = true)

                probarCategorias()

            } else {
                Log.w("API_TEST", "Login fallido: ${response.message}")
                _estado.value = _estado.value.copy(loginSuccess = false)
            }

        } catch (e: Exception) {
            Log.e("API_TEST", "Error durante login: ${e.message}", e)
            _estado.value = _estado.value.copy(loginSuccess = false)
        } finally {
            _estado.value = _estado.value.copy(LoadingLogin = false)
        }
    }

    suspend fun probarCategorias() {
        try {
            Log.d("API_TEST", "Iniciando solicitud GET /common/categorias...")

            val response = ClienteApi.service.obtenerCategorias()

            Log.d("API_TEST", "Respuesta completa: $response")

            if (response.success) {
                val categorias = response.data ?: emptyList()
                Log.d("API_TEST", "Categorías obtenidas correctamente (${categorias.size})")

                categorias.forEach { cat ->
                    Log.d("API_TEST", "ID: ${cat.idCategoria} | Nombre: ${cat.nombreCategoria}")
                }

                if (categorias.isEmpty()) {
                    Log.w("API_TEST", "El backend devolvió una lista vacía de categorías.")
                }

            } else {
                Log.w("API_TEST", "La API devolvió success=false o error inesperado: $response")
            }

        } catch (e: Exception) {
            Log.e("API_TEST", "Error al obtener categorías: ${e.message}", e)
        }
    }

    // ---------------------
    // Función de logout
    // ---------------------

    /**
     * Cierra la sesión del usuario.
     *
     * Borra credenciales y actualiza el estado de login a `false`.
     */
    fun signOut() {
        viewModelScope.launch {
            managerSesion.clearAuthToken()
            ClienteApi.actualizarToken(null)
            modelo.limpiarDatos()
            borrarDatos()
            actualizarEstaLoggeado(false)
            Log.d("AUTH_STATE", "User signed out. loginSuccess is now false. Token cleared")
        }
    }

    suspend fun LoginNegocio(){
        val request = LoginRequest(
            email = obtenerCredencial(),
            password = obtenerContrasena()
        )

        _estado.value = _estado.value.copy(LoadingLogin = true)

        try {
            val response = ClienteApi.service.login(request)
            Log.d("API_TEST", "Success: ${response.success}, Message: ${response.message}")

            if (response.success) {
                if (response.user?.role == "dueno") {
                    // Guardar datos del usuario en el modelo
                    modelo.setCorreo(response.user?.email ?: "")
                    modelo.setToken(response.token)
                    modelo.setRol(response.user?.role)
                    modelo.setNombre(response.user?.nombre)
                    modelo.setFolio(response.user?.folio)

                    ClienteApi.actualizarToken(response.token)

                    // Logs de depuración
                    Log.d(
                        "BUSINESS_SESSION", """
                        Sesión iniciada correctamente
                        Correo: ${modelo.correo}
                        Rol: ${modelo.rol}
                        Token: ${modelo.token?.take(40)}... (truncado)
                    """.trimIndent()
                    )

                    // Actualizar estado de login
                    _estado.value = _estado.value.copy(loginBusiness = true)


                } else {
                    Log.w("API_TEST", "El usuario no es un negocio, no se puede iniciar sesión.")
                    _estado.value = _estado.value.copy(loginBusiness = false)
                }
            } else {
                Log.w("API_TEST", "Login fallido: ${response.message}")
                _estado.value = _estado.value.copy(loginBusiness = false)
            }

        } catch (e: Exception) {
            Log.e("API_TEST", "Error durante login: ${e.message}", e)
            _estado.value = _estado.value.copy(loginBusiness = false)
        } finally {
            _estado.value = _estado.value.copy(LoadingLogin = false)
        }
    }
}
