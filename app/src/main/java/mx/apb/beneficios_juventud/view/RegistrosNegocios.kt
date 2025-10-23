import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mx.apb.beneficios_juventud.view.BottomBarNegocios
import mx.apb.beneficios_juventud.view.Pantalla
import mx.apb.beneficios_juventud.view.TopBarNegocioMenu
import mx.apb.beneficios_juventud.viewmodel.RegistrosVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrosNegocios(navController: NavController) {
    val currentRoute = Pantalla.RUTA_REGISTROS_NEGOCIOS
    val registrosVM: RegistrosVM = viewModel()
    val estado by registrosVM.estado.collectAsState()

    LaunchedEffect(Unit) {
        registrosVM.cargarRegistros() // ya no pasamos idDueno
    }

    Scaffold(
        topBar = { TopBarNegocioMenu("Registros") },
        bottomBar = { BottomBarNegocios(navController, currentRoute) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(estado.registros) { registro ->
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
