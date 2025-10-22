package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.viewmodel.DetalleEstablecimientoVM
import mx.apb.beneficios_juventud.viewmodel.PromosUiState

@Composable
fun CatalogoNegocio(
    navController: NavHostController,
    idEstablecimiento: Int? = null
) {
    val vm: DetalleEstablecimientoVM = viewModel(
        factory = DetalleEstablecimientoVM.factory(ClienteApi.service)
    )
    val promosState by vm.promos.collectAsState()

    // Diagnóstico: confirma que llegó el id
    LaunchedEffect(idEstablecimiento) {
        println(">> CatalogoNegocio idEstablecimiento=$idEstablecimiento")
        idEstablecimiento?.takeIf { it > 0 }?.let { vm.cargarPromociones(it) }
    }

    when {
        idEstablecimiento == null || idEstablecimiento <= 0 ->
            Text("Sin establecimiento seleccionado", modifier = Modifier.padding(16.dp))

        promosState.cargando ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        promosState.error != null ->
            Text("Error: ${promosState.error}", modifier = Modifier.padding(16.dp))

        promosState.items.isEmpty() ->
            Text("No hay promociones disponibles", modifier = Modifier.padding(16.dp))

        else ->
            ListaPromos(promosState)
    }
}


@Composable
private fun ListaPromos(estado: PromosUiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(estado.items) { p ->
            Card {
                Column(Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = p.establecimientoLogoUrl,
                            contentDescription = p.establecimientoNombre,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(p.establecimientoNombre, style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(p.titulo, style = MaterialTheme.typography.titleMedium)
                    if (!p.descripcion.isNullOrBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(p.descripcion!!)
                    }
                    if (p.imagenUrl != null) {
                        Spacer(Modifier.height(8.dp))
                        AsyncImage(model = p.imagenUrl, contentDescription = p.titulo)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(if (p.vigente) "Vigente" else "No vigente", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

