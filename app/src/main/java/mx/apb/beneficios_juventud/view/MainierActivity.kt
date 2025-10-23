package mx.apb.beneficios_juventud

import RegistrosNegocios
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.view.*
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * Actividad principal de la aplicación Beneficios Juventud.
 * @author Israel González Huerta
 * @author Adrián Proaño Bernal
 *
 * Gestiona la inicialización de la interfaz de usuario y el ViewModel principal.
 */
class MainActivity : ComponentActivity() {
    /** ViewModel compartido en toda la aplicación para manejar el estado global. */
    private val beneficiosVM: BeneficiosVM by viewModels()

    /**
     * Punto de entrada del ciclo de vida de la actividad.
     *
     * Configura el contenido principal de la interfaz mediante Compose.
     *
     * @param savedInstanceState estado guardado de la instancia (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(beneficiosVM)
        }
    }
}

/**
 * Composable principal que contiene toda la estructura de navegación
 * y la barra inferior (BottomBar).
 *
 * @param beneficiosVM instancia del ViewModel principal.
 */
@Composable
fun MainScreen(beneficiosVM: BeneficiosVM) {
    val perfil by beneficiosVM.perfil.collectAsState()

    val navController = rememberNavController()
    val bottomBarItems = Pantalla.pantallasBottomBar
    val estado by beneficiosVM.estado.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    var falla by remember { mutableStateOf(false) }

    if (estado.expiredSess) {
        AlertDialog(
            onDismissRequest = { /* No permitir cerrar el diálogo tocando fuera */ },
            title = { Text("Sesión Expirada") },
            text = { Text("Tu sesión ha finalizado. Por favor, inicia sesión de nuevo para continuar.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Llama a la función del ViewModel para manejar el cierre del diálogo y el logout
                        beneficiosVM.onSessionExpiredDialogDismissed()
                        // Navega a la pantalla de login
                        navController.navigate(Pantalla.RUTA_LOGIN) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
    val whiteScheme = lightColorScheme(
        onPrimary = Color.White,
        surface = Color.White,
        background = Color.White
    )

    MaterialTheme(colorScheme = whiteScheme) {
        if (estado.verifyingSess) {
            Scaffold(containerColor = Color.White) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.White)
                ) {
                    Loadingicon()
                }
            }
        } else {
            val startDest = if (estado.loginSuccess) Pantalla.RUTA_MAPA else Pantalla.RUTA_LOGIN
            Scaffold(
                containerColor = Color.White,
                bottomBar = {
                    if (currentRoute in bottomBarItems) {
                        BottomBar(navController, bottomBarItems)
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(innerPadding)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDest,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable(Pantalla.RUTA_LOGIN) {
                            Login(
                                loginClick = {
                                    scope.launch {
                                        beneficiosVM.Login()
                                        if (!beneficiosVM.estado.value.loginSuccess) {
                                            falla = true
                                        }
                                    }
                                },
                                beneficiosVM = beneficiosVM,
                                navController
                            )
                        }
                        composable(Pantalla.RUTA_OLVIDASTE) {
                            Olvidaste(beneficiosVM, navController)
                        }

                        composable(Pantalla.RUTA_MAPA) { Mapa(beneficiosVM, navController, perfil = perfil) }
                        composable(Pantalla.RUTA_MENU) { Menu(navController, perfil = perfil) }
                        composable(Pantalla.RUTA_AVISOS) { Avisos(navController, beneficiosVM, perfil = perfil) }
                        composable(Pantalla.RUTA_PERFIL) { Perfil(navController, beneficiosVM) }
                        composable(Pantalla.RUTA_LOGIN_NEGOCIOS) {
                            LoginNegocios(beneficiosVM, navController)
                        }
                        composable(Pantalla.RUTA_CATALOGO) { CatalogoNegocio(navController = navController, beneficiosVM = beneficiosVM) }

                        composable(
                            route = "${Pantalla.RUTA_CATALOGO}/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1
                            CatalogoNegocio(navController = navController, idEstablecimiento = id, beneficiosVM = beneficiosVM)
                        }

                        composable(
                            route = "qr/{idPromocion}",
                            arguments = listOf(navArgument("idPromocion"){ type = NavType.IntType }) // ✅ Int
                        ) { backStackEntry ->
                            val idInt = backStackEntry.arguments!!.getInt("idPromocion")
                            PromoQr(idPromocion = idInt.toLong(), beneficiosVM = beneficiosVM)
                        }

                        composable(Pantalla.RUTA_MENU_NEGOCIOS) { MenuNegocios(navController, beneficiosVM) }
                        composable(Pantalla.RUTA_SCANNER_NEGOCIOS) { ScannerNegocios(navController, beneficiosVM) }
                        composable(Pantalla.RUTA_REGISTROS_NEGOCIOS) { RegistrosNegocios(navController) }
                        composable(Pantalla.RUTA_VALIDAR_FOLIO) { ValidarFolio(navController) }
                        composable(Pantalla.RUTA_OFERTA_POR_FOLIO) { OfertaPorFolio(navController) }
                    }
                }
            }
        }

        if (falla) {
            AvisoError(
                onDismiss = {
                    beneficiosVM.borrarDatos()
                    falla = false
                },
                errorMessage = "Credenciales incorrectas"
            )
        }
    }
}

/**
 * Barra de navegación inferior (Bottom Navigation Bar)
 * que permite moverse entre las principales pantallas de la app.
 *
 * @param navController controlador de navegación para manejar los cambios de pantalla.
 * @param items lista de rutas disponibles en la barra inferior.
 */
@Composable
fun BottomBar(navController: NavHostController, items: List<String>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val icon = when (screen) {
                Pantalla.RUTA_MAPA -> Icons.Default.LocationOn
                Pantalla.RUTA_MENU -> Icons.Default.Menu
                Pantalla.RUTA_AVISOS -> Icons.Default.Notifications
                Pantalla.RUTA_PERFIL -> Icons.Default.AccountCircle
                else -> Icons.Default.Menu
            }
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = screen) },
                label = { Text(screen) },
                selected = currentRoute?.startsWith(screen) == true,
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

/**
 * Diálogo de error reutilizable.
 *
 * Muestra un mensaje de aviso al usuario y un botón para reintentar.
 *
 * @param onDismiss acción ejecutada al cerrar el diálogo.
 * @param errorMessage mensaje de error que se mostrará.
 */
@Composable
fun AvisoError(onDismiss: () -> Unit, errorMessage: String) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Reintentar") } },
        title = { Text("Aviso") },
        text = { Text(errorMessage) },
        icon = {}
    )
}

@Composable
fun Loadingicon() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            strokeWidth = 6.dp
        )
    }
}


/**
 * Composable simple que muestra texto centrado en la pantalla.
 *
 * @param name texto que se mostrará.
 */
/**
@Composable
fun ScreenText(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}
 */
