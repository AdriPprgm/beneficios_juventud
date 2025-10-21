package mx.apb.beneficios_juventud.viewmodel

import mx.apb.beneficios_juventud.model.BeneficiarioDetalle
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.UsoBeneficios

/**
 * Representa los diferentes estados posibles del perfil de usuario dentro de la aplicación.
 *
 * Esta interfaz sellada se utiliza para modelar los estados de carga, éxito o error
 * durante la obtención de la información del perfil y del historial de uso de beneficios.
 *
 * Es comúnmente observada desde la capa de vista (UI) mediante un [StateFlow]
 * dentro del [PerfilVM], permitiendo que la interfaz reaccione de manera declarativa
 * a los cambios de estado.
 */
sealed interface EstadoPerfil {
/**
 * Estado que indica que los datos del perfil están siendo cargados.
 */
    data object Loading : EstadoPerfil
    // Estado que representa una carga exitosa de los datos del perfil del usuario.
    data class Exito(
        val perfil: PerfilUsuario,
        val historial: List<UsoBeneficios>,
        val detalle: BeneficiarioDetalle? = null
    ) : EstadoPerfil
    // Estado que representa un error al intentar cargar el perfil o el historial.
    data class Error(val mensaje: String) : EstadoPerfil
}