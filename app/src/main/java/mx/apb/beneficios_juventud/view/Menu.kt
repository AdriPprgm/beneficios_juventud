package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.model.API.response.Categoria
import mx.apb.beneficios_juventud.model.ClasesMenu
import mx.apb.beneficios_juventud.model.ofertas
import mx.apb.beneficios_juventud.viewmodel.MenuVM

/**
 * Composable principal que muestra el menú de ofertas.
 * @author Israel González Huerta
 * @author Juan Pablo Solis Gomez
 *
 * @param navController controlador de navegación para moverse entre pantallas.
 */
@Composable
fun Menu(navController: NavHostController, vm: MenuVM = viewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Menú principal",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                TextField(
                    value = state.search,
                    onValueChange = vm::onSearchChange,
                    placeholder = { Text("Buscar en el menú") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Divisor()

                // Filtro de categorías dinámico (desde API)
                FiltroCategorias(
                    categorias = state.categorias,
                    seleccionadas = state.seleccionadas,
                    onToggle = vm::toggleCategoria,
                    onToggleTodas = vm::toggleTodas
                )

                Divisor()
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.loading) {
                item {
                    Text("Cargando…", modifier = Modifier.padding(16.dp))
                }
            }

            if (!state.loading && state.items.isEmpty()) {
                item {
                    Text("Sin resultados", modifier = Modifier.padding(16.dp))
                }
            }

            items(state.items) { e ->
                EstablecimientoCard(
                    nombre = e.nombre,
                    imagenUrl = e.logoURL,
                    categorias = e.categorias,
                    onClick = {
                        // pásale el id al detalle si lo requieres
                        navController.navigate("${Pantalla.RUTA_CATALOGO}/${e.idEstablecimiento}")
                    }
                )
            }
        }
    }
}

@Composable
private fun EstablecimientoCard(
    nombre: String,
    imagenUrl: String?,
    categorias: List<String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Imagen desde URL (como antes, ancho completo y alto fijo)
            AsyncImage(
                model = imagenUrl,
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            if (categorias.isNotEmpty()) {
                Text(
                    text = categorias.joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun FiltroCategorias(
    categorias: List<Categoria>,
    seleccionadas: Set<Int>,
    onToggle: (Int) -> Unit,
    onToggleTodas: () -> Unit
) {
    val todasActivadas = categorias.isNotEmpty() && seleccionadas.size == categorias.size

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = todasActivadas,
            onClick = onToggleTodas,
            label = { Text("Todas") }
        )

        categorias.forEach { c ->
            FilterChip(
                selected = c.idCategoria in seleccionadas,
                onClick = { onToggle(c.idCategoria) },
                label = { Text(c.nombreCategoria) }
            )
        }
    }
}


/**
 * Composable que dibuja un divisor horizontal.
 */
@Composable
fun Divisor() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        thickness = 1.dp,
        color = Color.LightGray
    )
}
