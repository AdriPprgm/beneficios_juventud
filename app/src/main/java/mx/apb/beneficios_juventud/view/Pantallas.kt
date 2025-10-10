package mx.apb.beneficios_juventud.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Pantalla (
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)
{
    companion object {
        const val RUTA_LOGIN = "Login"
        const val RUTA_MAPA = "Mapa"
        const val RUTA_MENU = "Menu"
        const val RUTA_NOTIFICACIONES = "Notificaciones"
        const val RUTA_PERFIL = "Perfil"
        const val RUTA_OLVIDASTE = "Olvidaste"
        const val RUTA_LOGIN_NEGOCIOS = "LoginNegocios"
        const val RUTA_CATALOGO = "Catalogo"

        // Lista de pantallas que aparecene en el bottom bar
        val pantallasBottomBar = listOf(
            RUTA_MAPA,
            RUTA_MENU,
            RUTA_NOTIFICACIONES,
            RUTA_PERFIL)
    }
    // Objeto para los botones del bottom bar
    private data object PantallaMapa:
        Pantalla(RUTA_MAPA, "Mapa", Icons.Default.LocationOn)
    private data object PantallaMenu:
        Pantalla(RUTA_MENU, "Menú", Icons.Default.Menu)
    private data object PantallaNotificaciones:
            Pantalla(RUTA_NOTIFICACIONES, "Notificaciones", Icons.Default.Notifications)
    private data object PantallaPerfil:
            Pantalla(RUTA_PERFIL, "Perfil", Icons.Default.AccountCircle)
}