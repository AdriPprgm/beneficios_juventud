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
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.model.ClasesMenu
import mx.apb.beneficios_juventud.model.ofertas

/**
 * Composable principal que muestra el menú de ofertas.
 * @author Israel González Huerta
 *
 * @param navController controlador de navegación para moverse entre pantallas.
 */
@Composable
fun Menu(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

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
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar en el menú") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Divisor()

                // Filtro de categorías con estado interno
                FiltroCategorias()

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
            items(ofertas) { oferta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Pantalla.RUTA_CATALOGO) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = oferta.imagenRes),
                            contentDescription = oferta.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = oferta.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Text(
                            text = oferta.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable que muestra un filtro de categorías usando [FilterChip].
 * Permite seleccionar múltiples categorías o activar "Todas" para seleccionar/desactivar todo.
 */
@Composable
fun FiltroCategorias() {
    var clasesMenus by remember {
        mutableStateOf(
            listOf(
                ClasesMenu("Salud", false),
                ClasesMenu("Belleza", false),
                ClasesMenu("Entretenimiento", false),
                ClasesMenu("Moda", false),
                ClasesMenu("Comida", false),
                ClasesMenu("Educación", false)
            )
        )
    }

    val todasActivadas = clasesMenus.all { it.activada }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip para seleccionar/deseleccionar todas las categorías
        FilterChip(
            selected = todasActivadas,
            onClick = {
                val nuevoEstado = !todasActivadas
                clasesMenus = clasesMenus.map { it.copy(activada = nuevoEstado) }
            },
            label = { Text("Todas") }
        )

        // Chips para categorías individuales
        clasesMenus.forEachIndexed { index, categoria ->
            FilterChip(
                selected = categoria.activada,
                onClick = {
                    clasesMenus = clasesMenus.mapIndexed { i, c ->
                        if (i == index) c.copy(activada = !c.activada) else c
                    }
                },
                label = { Text(categoria.nombre) }
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
