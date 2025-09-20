package mx.apb.beneficios_juventud.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Pantalla (
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)
{
    companion object {
        // var listaPantallas = listOf() //Quizás sea útil para el bottom bar
        const val RUTA_LOGIN = "Login"
        const val RUTA_MENU = "Menu"
        const val RUTA_MAPA = "Mapa"
        const val RUTA_NOTIFICACIONES = "Notificaciones"
        const val RUTA_PERFIL = "Perfil"
    }
    /**
     * Eliminar en caso de que no se use
    private data object PantallaLogin:
            Pantalla(RUTA_LOGIN, "Login", Icons.Default.AccountBox)
    */
    private data object PantallaMenu:
            Pantalla(RUTA_MENU, "Menú", Icons.Default.Menu)
}