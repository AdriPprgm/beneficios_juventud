package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerNegocios(navController: NavController) {
    val currentRoute = Pantalla.RUTA_SCANNER_NEGOCIOS

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear código") }
            )
        },
        bottomBar = {
            BottomBarNegocios(navController, currentRoute)
        }
    ) { innerPadding ->
        // Contenido principal del scanner
        Text(
            text = "Pantalla de scanner insano",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
