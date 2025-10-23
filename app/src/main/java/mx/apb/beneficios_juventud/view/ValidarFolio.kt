package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.viewmodel.FolioVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidarFolio(navController: NavController) {
    val currentRoute = Pantalla.RUTA_VALIDAR_FOLIO
    var folio by remember { mutableStateOf("") }
    val folioVM: FolioVM = viewModel()
    val estado by folioVM.estado.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBarNegocioMenu("Validar folio") },
        bottomBar = { BottomBarNegocios(navController, currentRoute) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = folio,
                    onValueChange = { folio = it },
                    label = { Text("Folio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            folioVM.buscarFolio(folio)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar")
                }

                Spacer(modifier = Modifier.padding(16.dp))

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Datos del beneficiario", style = MaterialTheme.typography.titleMedium)

                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text("Nombre: ", modifier = Modifier.weight(1f))
                            Text(estado.nombre, modifier = Modifier.weight(2f))
                        }

                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text("Edad: ", modifier = Modifier.weight(1f))
                            Text(estado.edad, modifier = Modifier.weight(2f))
                        }
                    }
                }
            }
        }
    }
}
