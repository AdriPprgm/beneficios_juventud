import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.apb.beneficios_juventud.view.BottomBarNegocios
import mx.apb.beneficios_juventud.view.Pantalla
import mx.apb.beneficios_juventud.view.TopBarNegocioMenu

data class Registro(
    val nombreUsuario: String,
    val folio: String,
    val nombreOferta: String,
    val fechaHora: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrosNegocios(navController: NavController) {
    val currentRoute = Pantalla.RUTA_REGISTROS_NEGOCIOS

    // Datos de ejemplo
    val registros = remember {
        listOf(
            Registro("Juan Pérez", "12345", "Descuento Six Flags", "2025-10-22 12:30"),
            Registro("María López", "12346", "Combo Restaurante", "2025-10-22 13:00"),
            Registro("Carlos Sánchez", "12347", "Merchandising", "2025-10-22 13:15")
        )
    }

    Scaffold(
        topBar = {
            TopBarNegocioMenu("Registros")
        },
        bottomBar = {
            BottomBarNegocios(navController, currentRoute)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(registros) { registro ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Usuario: ${registro.nombreUsuario}", fontSize = 16.sp)
                        Text("Folio: ${registro.folio}", fontSize = 16.sp)
                        Text("Oferta: ${registro.nombreOferta}", fontSize = 16.sp)
                        Text(
                            "Fecha y hora: ${registro.fechaHora}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
