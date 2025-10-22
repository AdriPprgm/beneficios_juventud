package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.R
import mx.apb.beneficios_juventud.model.OfertaNegocio
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import mx.apb.beneficios_juventud.viewmodel.NegocioVM

/**
 * Pantalla principal para la gestión de ofertas por parte de los negocios.
 * Permite visualizar, agregar y eliminar ofertas en una lista dinámica.
 *
 * @param navController Controlador de navegación utilizado para manejar transiciones entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuNegocios(
    navController: NavController,
    beneficiosVM: BeneficiosVM,
    negocioVM: NegocioVM = viewModel()
) {
    val estado by negocioVM.estado.collectAsState()

    var mostrarDialogo by remember { mutableStateOf(false) }

    // 🔹 Estado para saber en qué pantalla estás
    val currentRoute = remember { mutableStateOf(Pantalla.RUTA_MENU_NEGOCIOS) }

    LaunchedEffect(Unit) {
        val ownerId = beneficiosVM.obtenerId()
        negocioVM.setIdDuenoYRecargar(ownerId)
    }

    Scaffold(
        topBar = {
            TopBarNegocioMenu("Administrar ofertas")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogo = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar oferta",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        bottomBar = {
            BottomBarNegocios(navController, currentRoute.value)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(estado.ofertas, key = { it.id }) { oferta ->
                OfertaNegocioCard(
                    oferta = oferta,
                    onDelete = {
                        // TODO: Implementar la llamada al ViewModel para eliminar la oferta en el backend
                        // negocioVM.eliminarOferta(it)
                    },
                    onClick = {
                        navController.navigate(Pantalla.RUTA_OFERTA_POR_FOLIO)
                    }
                )
            }
        }
    }

    if (mostrarDialogo) {
        DialogAgregarOferta(
            onDismiss = { mostrarDialogo = false },
            onSave = { titulo, descripcion ->
                // TODO: Implementar la llamada al ViewModel para guardar la nueva oferta
                // negocioVM.agregarOferta(titulo, descripcion)
                mostrarDialogo = false
            }
        )
    }
}


/**
 * Barra superior de la pantalla de negocio.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNegocioMenu(
    title: String
) {
    Column {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,          // Fondo blanco
                titleContentColor = Color.Black,       // Texto negro
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )
        Divisor()
    }
}


/**
 * Tarjeta visual que muestra la información de una oferta creada po r el negocio.
 *
 * @param oferta Objeto [OfertaNegocio] con los datos de la oferta.
 * @param onDelete Acción ejecutada cuando el usuario selecciona eliminar una oferta.
 */
@Composable
fun OfertaNegocioCard(
    oferta: OfertaNegocio,
    onDelete: (OfertaNegocio) -> Unit,
    onClick: (OfertaNegocio) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(oferta) }, // Hacer clickable toda la tarjeta
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // --- CHANGE TO AsyncImage TO LOAD FROM URL ---
            AsyncImage(
                model = oferta.imagenURL, // Use the URL from the data model
                contentDescription = oferta.titulo,
                placeholder = painterResource(id = R.drawable.tarjeta_benefico), // Fallback image
                error = painterResource(id = R.drawable.tarjeta_benefico),       // Fallback image on error
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = oferta.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = oferta.descripcion,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onDelete(oferta) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                Spacer(Modifier.width(4.dp))
                Text("Eliminar")
            }
        }
    }
}


/**
 * Cuadro de diálogo para agregar una nueva oferta.
 * Contiene campos de texto para título y descripción, además de un botón de imagen.
 *
 * @param onDismiss Acción ejecutada cuando se cierra el cuadro de diálogo sin guardar.
 * @param onSave Acción ejecutada al guardar una nueva oferta con los datos proporcionados.
 */
@Composable
fun DialogAgregarOferta(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                        onSave(titulo, descripcion)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Nueva oferta") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { /* TODO: agregar funcionalidad más adelante */ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar imagen")
                    Spacer(Modifier.width(6.dp))
                    Text("Agregar imagen")
                }
            }
        }
    )
}

@Composable
fun BottomBarNegocios(navController: NavController, currentRoute: String) {
    NavigationBar {
        val items = listOf(
            "Menú" to Pantalla.RUTA_MENU_NEGOCIOS,
            "Scanner" to Pantalla.RUTA_SCANNER_NEGOCIOS,
            "Registros" to Pantalla.RUTA_REGISTROS_NEGOCIOS,
            "Validar folio" to Pantalla.RUTA_VALIDAR_FOLIO
        )

        items.forEach { (label, route) ->
            val icon = when (label) {
                "Menú" -> Icons.Default.Menu
                "Scanner" -> Icons.Default.QrCodeScanner
                "Registros" -> Icons.Default.History
                "Validar folio" -> Icons.Default.Search
                else -> Icons.Default.Menu
            }

            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
