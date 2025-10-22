package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidarFolio(navController: NavController) {
    val currentRoute = Pantalla.RUTA_VALIDAR_FOLIO
    var folio by remember { mutableStateOf("") }    // Estado para folio
    var nombre by remember { mutableStateOf("Juan Pérez") }   // Ejemplo de nombre
    var edad by remember { mutableStateOf("25") }     // Ejemplo de edad

    Scaffold(
        topBar = {
            TopBarNegocioMenu("Validar folio")
        },
        bottomBar = {
            BottomBarNegocios(navController, currentRoute)
        }
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
                // Campo de búsqueda de folio
                OutlinedTextField(
                    value = folio,
                    onValueChange = { folio = it },
                    label = { Text("Folio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    onClick = { /* Por ahora no hace nada */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buscar")
                }

                Spacer(modifier = Modifier.padding(16.dp))

                // Sección con relieve para los datos del beneficiario (solo lectura)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Datos del beneficiario",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Nombre (solo lectura)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nombre: ",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(2f)
                            )
                        }

                        // Edad (solo lectura)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Edad: ",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = edad,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(2f)
                            )
                        }
                    }
                }
            }
        }
    }
}
