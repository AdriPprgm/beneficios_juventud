package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.viewmodel.PerfilVM
import mx.apb.beneficios_juventud.viewmodel.EstadoPerfil
import mx.apb.beneficios_juventud.model.CategoriaBeneficios
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.UsoBeneficios
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import java.time.format.DateTimeFormatter

@Composable
fun Perfil(navController: NavHostController,
    beneficiosVM: BeneficiosVM = viewModel())
    {
        val estado by beneficiosVM.estado.collectAsState()
        val vm: PerfilVM = viewModel()
        val state by vm.uiState.collectAsState()
        var mostrarConfirmacion by remember { mutableStateOf(false) }
        val vm1: BeneficiosVM = viewModel()


    if (!estado.loginSuccess){
        navController.navigate(Pantalla.RUTA_LOGIN)
    }

    if (mostrarConfirmacion) {
        ConfirmarLogout(
            onDismiss = { mostrarConfirmacion = false },
            onConfirm = { vm1.signOut() }
            )
    }

    Scaffold(
        topBar = { PerfilTopBar() }
    ) { innerPadding ->
        when (val s = state) {
            EstadoPerfil.Loading -> Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is EstadoPerfil.Error -> Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ocurrió un error: ${(s as EstadoPerfil.Error).mensaje}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.reload() }) { Text("Reintentar") }
                }
            }

            is EstadoPerfil.Exito -> {
                val perfil = (s as EstadoPerfil.Exito).perfil
                val historial = s.historial
                PerfilContent(
                    modifier = Modifier.padding(innerPadding),
                    perfil = perfil,
                    historial = historial,
                    onSignOut = { mostrarConfirmacion = true }
                )
            }
        }
    }
}

// UI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfilTopBar() {
    TopAppBar(
        title = { Text("Perfil de Usuario") }
    )
}

@Composable
private fun PerfilContent(
    modifier: Modifier = Modifier,
    perfil: PerfilUsuario,
    historial: List<UsoBeneficios>,
    onSignOut: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd 'de' LLLL, yyyy") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileCard(perfil = perfil)
        }

        item {
            Text(
                "Historial de beneficios usados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        items(historial, key = { it.id }) { uso ->
            BenefitItem(uso = uso, formatter = formatter)
        }

        item {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text("Cerrar sesión")
            }
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

/* ---------- Widgets ---------- */

@Composable
private fun ProfileCard(perfil: PerfilUsuario) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        // “Banner”
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        // Avatar + nombre + edad (como en el prototipo)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder circular
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = perfil.name.firstOrNull()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    perfil.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${perfil.age} años",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BenefitItem(uso: UsoBeneficios, formatter: DateTimeFormatter) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    uso.merchant,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                CategoryChip(uso.category)
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    uso.date.format(formatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                uso.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CategoryChip(c: CategoriaBeneficios) {
    val (label, icon) = when (c) {
        CategoriaBeneficios.ENTRETENIMIENTO -> "Entretenimiento" to Icons.Default.LocalMovies
        CategoriaBeneficios.SALUD -> "Salud" to Icons.Default.HealthAndSafety
        CategoriaBeneficios.EDUCACION -> "Educación" to Icons.Default.MenuBook
    }
    AssistChip(
        onClick = {},
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}

@Composable
fun ConfirmarLogout(onDismiss: () -> Unit, onConfirm: () -> Unit){
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {TextButton(onClick = onConfirm) { Text("Cerrar sesión") }},
        dismissButton = {TextButton(onClick = onDismiss) { Text("Regresar") }},
        title = { Text("Aviso") },
        text = { Text("¿Desea cerrar sesión?") })
}
