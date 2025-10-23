package mx.apb.beneficios_juventud.model.API

import mx.apb.beneficios_juventud.model.API.request.AgrgarOfertaRequest
import mx.apb.beneficios_juventud.model.API.request.ForgotRequest
import mx.apb.beneficios_juventud.model.API.request.LoginRequest
import mx.apb.beneficios_juventud.model.API.request.ScannerRequest
import mx.apb.beneficios_juventud.model.API.response.AgregarOfertaResponse
import mx.apb.beneficios_juventud.model.API.response.CategoriasResponse
import mx.apb.beneficios_juventud.model.API.response.EliminarOfertaResponse
import mx.apb.beneficios_juventud.model.API.response.FolioResponse
import mx.apb.beneficios_juventud.model.API.response.ForgotResponse
import mx.apb.beneficios_juventud.model.API.response.LoginResponse
import mx.apb.beneficios_juventud.model.API.response.OfertasNegocioResponse
import mx.apb.beneficios_juventud.model.API.response.PageResponse
import mx.apb.beneficios_juventud.model.API.response.PerfilResponse
import mx.apb.beneficios_juventud.model.API.response.PromocionNetwork
import mx.apb.beneficios_juventud.model.API.response.QrResponse
import mx.apb.beneficios_juventud.model.API.response.RegistrosResponse
import mx.apb.beneficios_juventud.model.API.response.ScannerResponse
import mx.apb.beneficios_juventud.model.API.response.SucursalesResponse
import mx.apb.beneficios_juventud.model.API.response.UbicacionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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

    @POST("mobile/verificar-qr")
    suspend fun scanner(@Body request: ScannerRequest): retrofit2.Response<ScannerResponse>

    // Obtener nombre y edad del usuario a partir de su folio
    @GET("mobile/validar-folio")
    suspend fun validarFolio(
        @Query("folio") folio: String
    ): retrofit2.Response<FolioResponse>

    @GET("mobile/registro-canje")
    suspend fun registroCanje(): RegistrosResponse



    @GET("mobile/establecimientos")
    suspend fun listarEstablecimientos(
        @Query("q") q: String? = null,
        @Query("categoryIds") categoryIds: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): PagedResponse<EstablecimientoItem>

    @GET("mobile/ubicacion-sucursales")
    suspend fun ubicarSucursales(): SucursalesResponse

    @GET("mobile/detalles-perfil")
    suspend fun getPerfil(): PerfilResponse

    @GET("mobile/ubicacion-sucursales")
    suspend fun ubicacionSucursales(): UbicacionResponse

    @GET("mobile/promociones-establecimiento-dueno")
    suspend fun ofertasNegocio(@Query ("idDueno") idDueno: Int?): OfertasNegocioResponse

    @POST("mobile/crear-nueva-promocion")
    suspend fun publicarOferta(@Body request: AgrgarOfertaRequest): AgregarOfertaResponse

    @POST("mobile/desactivar-promocion/{idPromocion}")
    suspend fun eliminarOferta(@Path("idPromocion") idPromocion: Int): EliminarOfertaResponse
    @GET("mobile/promociones-establecimiento")
    suspend fun promocionesPorEstablecimiento(
        @Query("idEstablecimiento") idEstablecimiento: Int,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): PageResponse<PromocionNetwork>

    @POST("mobile/generar-qr/{idPromocion}")
    suspend fun generarQr(
        @Path("idPromocion") idPromocion: Long,
        @Header("Authorization") bearer: String, // "Bearer <token>"
    ): QrResponse

    @POST("auth/forgot-password-email")
    suspend fun forgotPasswordEmail(@Body req: ForgotRequest): ForgotResponse

}