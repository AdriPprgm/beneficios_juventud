package mx.apb.beneficios_juventud.viewmodel

import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.UsoBeneficios

sealed interface EstadoPerfil {
    data object Loading : EstadoPerfil
    data class Exito(
        val perfil: PerfilUsuario,
        val historial: List<UsoBeneficios>
    ) : EstadoPerfil
    data class Error(val mensaje: String) : EstadoPerfil
}