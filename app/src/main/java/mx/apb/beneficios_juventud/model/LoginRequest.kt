package mx.apb.beneficios_juventud.model

data class LoginRequest(
    val credencial: String,
    val contrasena: String
)
