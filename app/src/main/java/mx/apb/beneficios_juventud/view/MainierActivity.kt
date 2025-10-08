package mx.apb.beneficios_juventud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.view.Login
import mx.apb.beneficios_juventud.view.LoginNegocios
import mx.apb.beneficios_juventud.view.Mapa
import mx.apb.beneficios_juventud.view.Olvidaste
import mx.apb.beneficios_juventud.view.Pantalla
import mx.apb.beneficios_juventud.view.Perfil
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM


class MainActivity : ComponentActivity() {
    private val beneficiosVM: BeneficiosVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(beneficiosVM)
        }
    }
}

@Composable
fun MainScreen(beneficiosVM: BeneficiosVM) {
    val navController = rememberNavController()
    val bottomBarItems = Pantalla.pantallasBottomBar

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            if (currentRoute in bottomBarItems) {
                BottomBar(navController, bottomBarItems)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Pantalla.RUTA_LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Pantalla.RUTA_LOGIN) {
                /**
                 * Posiblemente tenemos que cambiar la forma en la cual manejamos
                 * el login en las pantallas, ademas tenemos que re acomodar las pantallas
                 * principales, ya que creo que roman quiere la menor cantidad funciones
                 * en MainActivity
                 * @author Adrian Proano Bernal
                 */
                Login(
                    loginClick = {
                        scope.launch {
                            beneficiosVM.testLogin()
                            if (beneficiosVM.estado.value.loginSuccess) {
                                navController.navigate(Pantalla.RUTA_MAPA) {
                                    popUpTo(Pantalla.RUTA_LOGIN) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Pantalla.RUTA_LOGIN)
                            }
                        }
                    },
                    beneficiosVM = beneficiosVM,
                    navController
                )
            }
            composable(Pantalla.RUTA_OLVIDASTE) {
                ScreenText("Olvidaste") // placeholder
            }
            composable(Pantalla.RUTA_OLVIDASTE) {
                Olvidaste(beneficiosVM, navController)

            }
            composable(Pantalla.RUTA_MAPA) { Mapa(beneficiosVM) }
            composable(Pantalla.RUTA_MENU) { ScreenText("Menu") }
            composable(Pantalla.RUTA_NOTIFICACIONES) { ScreenText("Notificaciones") }
            composable(Pantalla.RUTA_PERFIL) { Perfil() }
            composable(Pantalla.RUTA_LOGIN_NEGOCIOS) { LoginNegocios(beneficiosVM, navController) }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, items: List<String>){
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val icon = when (screen) {
                Pantalla.RUTA_MAPA -> Icons.Default.LocationOn
                Pantalla.RUTA_MENU -> Icons.Default.Menu
                Pantalla.RUTA_NOTIFICACIONES -> Icons.Default.Notifications
                Pantalla.RUTA_PERFIL -> Icons.Default.AccountCircle
                else -> Icons.Default.Menu // No estoy muy segura de qué hace esta línea
            }
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = screen) },
                label = { Text(screen) },
                selected = currentRoute == screen,
                onClick = {
                    navController.navigate(screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ScreenText(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}