package mx.apb.beneficios_juventud.model.API

import mx.apb.beneficios_juventud.model.API.request.LoginRequest
import mx.apb.beneficios_juventud.model.API.response.CategoriasResponse
import mx.apb.beneficios_juventud.model.API.response.LoginResponse
import mx.apb.beneficios_juventud.model.API.response.SucursalesResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query


data class Meta(val page:Int, val pageSize:Int, val total:Int)
data class EstablecimientoItem(
    val idEstablecimiento: Int,
    val nombre: String,
    val logoURL: String?,
    val categorias: List<String>
)
data class PagedResponse<T>(val success:Boolean, val data: List<T>?, val meta: Meta?)
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

    @GET("mobile/establecimientos")
    suspend fun listarEstablecimientos(
        @Query("q") q: String? = null,
        @Query("categoryIds") categoryIds: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): PagedResponse<EstablecimientoItem>

    @GET("mobile/ubicacion-sucursales")
    suspend fun ubicarSucursales(): SucursalesResponse

}