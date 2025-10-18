package mx.apb.beneficios_juventud.model.API

import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente singleton para comunicación con el backend de Beneficios Juventud.
 */
object ClienteApi {

    private const val BASE_URL = "https://fgdmbhrw5b.execute-api.us-east-2.amazonaws.com/dev/"

    // Referencia global al modelo de sesión
    private val modelo = BeneficiosJuventud()

    // Interceptor para logs (opcional, útil para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor que agrega el token JWT
    private val authInterceptor = AuthInterceptor(modelo)

    // Cliente HTTP con ambos interceptores
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofit configurado
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio que expone los endpoints
    val service: ServicioApi by lazy {
        retrofit.create(ServicioApi::class.java)
    }

    //Método para actualizar el token global desde el ViewModel
    fun actualizarToken(token: String?) {
        modelo.setToken(token)
    }
}