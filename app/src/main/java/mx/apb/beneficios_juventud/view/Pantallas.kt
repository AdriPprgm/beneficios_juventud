package mx.apb.beneficios_juventud.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @author: Israel González Huerta
 * Clase sellada que representa las pantallas de la aplicación.
 *
 * Cada pantalla tiene una ruta para navegación, una etiqueta para mostrar en UI
 * y un ícono asociado para barras de navegación o elementos visuales.
 *
 * @property ruta Identificador único de la pantalla usado en la navegación.
 * @property etiqueta Texto que representa la pantalla en la interfaz.
 * @property icono Ícono que se muestra en la barra de navegación o elementos relacionados.
 */
sealed class Pantalla(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
) {
    companion object {
        /** Ruta para la pantalla de inicio de sesión de usuario */
        const val RUTA_LOGIN = "Login"

        /** Ruta para la pantalla de mapa */
        const val RUTA_MAPA = "Mapa"

        /** Ruta para la pantalla del menú principal */
        const val RUTA_MENU = "Menu"

        /** Ruta para la pantalla de notificaciones/avisos */
        const val RUTA_AVISOS = "Avisos"

        /** Ruta para la pantalla de perfil */
        const val RUTA_PERFIL = "Perfil"

        /** Ruta para la pantalla de recuperación de contraseña */
        const val RUTA_OLVIDASTE = "Olvidaste"

        /** Ruta para la pantalla de inicio de sesión de negocios */
        const val RUTA_LOGIN_NEGOCIOS = "LoginNegocios"

        /** Ruta para la pantalla de catálogo de negocios */
        const val RUTA_CATALOGO = "Catalogo"

        /** Ruta para la pantalla de menú exclusivo de negocios */
        const val RUTA_MENU_NEGOCIOS = "MenuNegocios"

        const val RUTA_TARJETA = "tarjeta_digital"
        const val RUTA_SCANNER_NEGOCIOS = "ScannerNegocios"


        /**
         * Lista de rutas que aparecen en la barra inferior (BottomBar)
         */
        val pantallasBottomBar = listOf(
            RUTA_MAPA,
            RUTA_MENU,
            RUTA_AVISOS,
            RUTA_PERFIL
        )
    }

    // Objetos internos para cada pantalla de BottomBar
    private data object PantallaMapa :
        Pantalla(RUTA_MAPA, "Mapa", Icons.Default.LocationOn)

    private data object PantallaMenu :
        Pantalla(RUTA_MENU, "Menú", Icons.Default.Menu)

    private data object PantallaNotificaciones :
        Pantalla(RUTA_AVISOS, "Avisos", Icons.Default.Notifications)

    private data object PantallaPerfil :
        Pantalla(RUTA_PERFIL, "Perfil", Icons.Default.AccountCircle)
}
