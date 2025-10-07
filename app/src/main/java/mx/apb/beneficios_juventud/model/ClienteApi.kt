package mx.apb.beneficios_juventud.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            //https://ufa5c6ltpkvbuwapdie6c6npju0cwotk.lambda-url.us-east-1.on.aws/
            .baseUrl("https://fgdmbhrw5b.execute-api.us-east-2.amazonaws.com/dev/auth/login/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service by lazy {
        retrofit.create(ServicioApi::class.java)
    }
}