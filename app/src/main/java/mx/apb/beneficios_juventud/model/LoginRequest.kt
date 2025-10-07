package mx.apb.beneficios_juventud.model

data class LoginRequest private constructor(
    val credencial: String,
    val contrasena: String
) {
    companion object{
        /**
         * Factory method to create a new LoginRequest instance with hashed credentials.
         *
         * @param rawCredencial The plain-text user credential.
         * @param rawContrasena The plain-text user password.
         * @return A LoginRequest instance with both credentials hashed.
         */

        fun create(rawCredencial: String, rawContrasena: String): LoginRequest {
            val hashedContrasena = HashingUtils.sha256(rawContrasena)
            return LoginRequest(rawCredencial, hashedContrasena)
        }
    }
}
