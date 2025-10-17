package mx.apb.beneficios_juventud.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**
 * Objeto singleton que configura y provee el cliente de red para la comunicación
 * con la API del proyecto **Beneficios Juventud**.
 *
 * Utiliza la librería [Retrofit] para manejar las peticiones HTTP y [GsonConverterFactory]
 * para la conversión automática entre objetos JSON y clases de datos de Kotlin.
 *
 * Este cliente centraliza la configuración de la conexión con el backend, garantizando
 * que todas las solicitudes compartan la misma instancia de Retrofit (patrón *singleton*),
 * lo que mejora la eficiencia y evita la duplicación de recursos.
 */
object ClienteApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            //https://ufa5c6ltpkvbuwapdie6c6npju0cwotk.lambda-url.us-east-1.on.aws/
            //https://fgdmbhrw5b.execute-api.us-east-2.amazonaws.com/dev/auth/login/
            .baseUrl("https://ufa5c6ltpkvbuwapdie6c6npju0cwotk.lambda-url.us-east-1.on.aws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy {
        retrofit.create(ServicioApi::class.java)
    }
}