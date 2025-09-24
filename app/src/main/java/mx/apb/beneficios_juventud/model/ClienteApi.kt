package mx.apb.beneficios_juventud.model

import com.google.firebase.appdistribution.gradle.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://abc123.execute-api.us-east-1.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)
}