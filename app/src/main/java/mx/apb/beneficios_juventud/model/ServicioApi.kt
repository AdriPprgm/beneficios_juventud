package mx.apb.beneficios_juventud.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz que define los endpoints del servicio web del sistema
 * **Beneficios Juventud** utilizando la librería [Retrofit].
 *
 * Cada método representa una operación HTTP específica (como `POST` o `GET`)
 * que permite la comunicación con el backend para obtener o enviar información.
 *
 * En esta interfaz se declaran las rutas, los métodos HTTP y los tipos de datos
 * asociados a las solicitudes y respuestas, que Retrofit se encargará de
 * implementar dinámicamente en tiempo de ejecución.
 */

interface ServicioApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}