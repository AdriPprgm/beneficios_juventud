package mx.apb.beneficios_juventud.model

import androidx.compose.runtime.Immutable

@Immutable
data class PerfilUsuario(
    val name: String,
    val age: Int,
    val photoUrl: String? = null
)