package mx.apb.beneficios_juventud.model.API

import android.util.Log
import mx.apb.beneficios_juventud.model.BeneficiosJuventud
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Interceptor que agrega el token JWT a cada solicitud HTTP.
 */
class AuthInterceptor(
    private val modelo: BeneficiosJuventud,
    private val onSessExpired: () -> Unit) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Si el token no existe, la solicitud se envía sin modificar
        val token = modelo.token
        if (token.isNullOrEmpty()) {
            Log.w("AuthInterceptor", "No hay token disponible, se enviará la solicitud sin autenticación.")
            return chain.proceed(originalRequest)
        }

        // Agregar el header Authorization
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept", "application/json")
            .build()

        val response = chain.proceed(newRequest)

        // Comprobar si la respuesta es un error 401 (No autorizado)
        if (response.code == 401) {
            Log.e("AuthInterceptor", "Token expirado o inválido. Código 401 detectado.")

            // 1. Invocar el callback para que el ViewModel maneje el logout y muestre el diálogo.
            onSessExpired()

            // 2. Detener la cadena de respuesta para evitar que la excepción llegue a la UI.
            // Devolvemos una respuesta "falsa" pero válida para que la app no crashee.
            // Esto es crucial para evitar que la excepción se propague.
            return response.newBuilder()
                .code(200) // Cambiamos el código a uno no-erróneo.
                .body("{\"message\":\"Session expired\"}".toResponseBody(null)) // Cuerpo de respuesta genérico.
                .build()
        }

        Log.d("AuthInterceptor", "Token agregado al header")
        return response
    }
}