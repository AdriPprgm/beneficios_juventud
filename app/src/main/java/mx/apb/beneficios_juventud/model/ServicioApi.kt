package mx.apb.beneficios_juventud.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServicioApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Call<LoginResponse>
}