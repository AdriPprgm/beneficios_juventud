package mx.apb.beneficios_juventud.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ufa5c6ltpkvbuwapdie6c6npju0cwotk.lambda-url.us-east-1.on.aws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy {
        retrofit.create(ServicioApi::class.java)
    }
}