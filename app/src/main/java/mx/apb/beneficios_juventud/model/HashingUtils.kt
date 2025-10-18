package mx.apb.beneficios_juventud.model

import java.security.MessageDigest

object HashingUtils {
    /**
     * Hashea un input string usando el algoritmo SHA-256.
     *
     * @param input string a ser hasheado.
     * @return la representación hexadecimal del SHA-256 hash.
     * @author Adrian Proano Bernal
     */
    fun sha256(input: String): String{
        return try{
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
            // Convert byte array to a hexadecimal string
            hash.fold("") { str, it -> str + "%02x".format(it)}
        } catch (e: Exception){
            throw RuntimeException("Error al hashear el texto", e)
        }
    }
}