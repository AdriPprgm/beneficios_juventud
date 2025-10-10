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
    import androidx.compose.ui.*
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import mx.apb.beneficios_juventud.R // Asegúrate de tener imágenes de ejemplo en res/drawable

    data class Oferta(
        val imagenRes: Int,
        val titulo: String,
        val descripcion: String
    )

    @Composable
    fun Menu(navController: NavHostController) {
        var searchText by remember { mutableStateOf("") }

        val categorias = listOf(
            "Todas", "Salud", "Belleza", "Entretenimiento", "Moda", "Comida", "Educación"
        )

        var categoriaSeleccionada by remember { mutableStateOf("Todas") }

        // Lista de ejemplo de ofertas
        val ofertas = listOf(
            Oferta(R.drawable.oferta1, "Six Flags", "10% de descuento en pases de un día"),
        )

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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { categoria ->
                            FilterChip(
                                selected = categoria == categoriaSeleccionada,
                                onClick = { categoriaSeleccionada = categoria },
                                label = { Text(categoria) }
                            )
                        }
                    }

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
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = oferta.imagenRes),
                                contentDescription = oferta.titulo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp), // ajusta la altura como quieras
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


    @Composable
    fun Divisor()
    {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }