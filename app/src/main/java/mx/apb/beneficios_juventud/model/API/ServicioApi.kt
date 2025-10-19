package mx.apb.beneficios_juventud.model.API

import mx.apb.beneficios_juventud.model.API.request.LoginRequest
import mx.apb.beneficios_juventud.model.API.response.CategoriasResponse
import mx.apb.beneficios_juventud.model.API.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

/**
 * Definición de endpoints del backend de Beneficios Juventud.
 */
interface ServicioApi {

    /**
     * Endpoint de autenticación de usuarios.
     *
     * @param request Credenciales del usuario (email y password)
     * @return Respuesta con token JWT y datos del usuario
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("common/categorias")
    suspend fun obtenerCategorias(): CategoriasResponse
}