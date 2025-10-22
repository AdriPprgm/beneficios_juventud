package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.model.API.response.Categoria
import mx.apb.beneficios_juventud.viewmodel.MenuVM

/**
 *
 * @author Israel González Huerta
 * @author Juan Pablo Solis Gomez
 *
 * Composable principal que muestra el menú de ofertas.
 *
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
                    .padding(8.dp, 16.dp, 8.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Fila superior con título centrado e ícono a la derecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    // Título centrado
                    Text(
                        text = "Menú principal",
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Ícono de perfil clickable a la derecha
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .clickable {
                                navController.navigate(Pantalla.RUTA_PERFIL)
                            }
                    )
                }

                // Campo de búsqueda
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
                    id = e.idEstablecimiento,
                    nombre = e.nombre,
                    imagenUrl = e.logoURL,
                    categorias = e.categorias,
                    onClick = {

                        navController.navigate("${Pantalla.RUTA_CATALOGO}/${e.idEstablecimiento}")
                    }
                )
            }
        }
    }
}
/**
 * Composable que representa una tarjeta visual de un establecimiento dentro del catálogo.
 *
 * Esta tarjeta muestra la información principal de un establecimiento, incluyendo su imagen,
 * nombre y categorías, y permite ejecutar una acción al hacer clic sobre ella.
 *
 *
 * @param id identificador único del establecimiento.
 * @param nombre nombre del establecimiento a mostrar.
 * @param imagenUrl URL de la imagen o logotipo del establecimiento (puede ser `null`).
 * @param categorias lista de nombres de categorías asociadas al establecimiento (por ejemplo, “Café”, “Restaurante”).
 * @param onClick callback ejecutado cuando el usuario presiona la tarjeta.
 * Recibe como parámetro el `id` del establecimiento seleccionado.
 */
@Composable
private fun EstablecimientoCard(
    id: Int,
    nombre: String,
    imagenUrl: String?,
    categorias: List<String>,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = nombre,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp))
            if (categorias.isNotEmpty()) {
                Text(
                    categorias.joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}


/**
 * Composable que muestra una fila horizontal de filtros de categorías mediante chips interactivos.
 *
 * Este componente permite al usuario seleccionar o deseleccionar categorías individuales
 * para filtrar resultados (por ejemplo, establecimientos o promociones) dentro de la aplicación.
 * También incluye una opción especial **"Todas"** para seleccionar o limpiar todos los filtros a la vez.
 */
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
