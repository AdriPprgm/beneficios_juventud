package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import mx.apb.beneficios_juventud.viewmodel.MenuVM
import mx.apb.beneficios_juventud.viewmodel.NotificacionUI
import mx.apb.beneficios_juventud.viewmodel.NotificacionesVM

/**
 * Composable principal que muestra la lista de notificaciones del usuario.
 * @author Juan Pablo Solís Gómez
 *
 * @param navController Controlador de navegación para regresar a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Avisos(
    navController: NavHostController,
    beneficiosVM: BeneficiosVM,
    vm: MenuVM = viewModel(),
    perfil: PerfilUsuario?,
    notificacionesVM: NotificacionesVM = viewModel()
) {
    val estado by notificacionesVM.estado.collectAsState()
    val scope = rememberCoroutineScope()

    // Cargar una vez al abrir la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            notificacionesVM.cargarNotificaciones()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarAvisos(
                title = "Notificaciones",
                navController = navController,
                perfil = perfil
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(inner)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(estado.notificaciones.size) { idx ->
                NotificationCard(notificacion = estado.notificaciones[idx])
            }
        }
    }
}


/**
 * Barra superior de la pantalla de notificaciones.
 *
 * @param title Título que se muestra en la barra.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarAvisos(
    title: String,
    navController: NavHostController,
    vm: MenuVM = viewModel(), perfil: PerfilUsuario?
) {
    Column {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Spacer izquierdo para balancear la barra
                    Spacer(modifier = Modifier.width(72.dp)) // ancho similar al ícono de perfil + padding

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    // Spacer derecho igual al ancho del ícono
                    Spacer(modifier = Modifier.width(40.dp))
                }
            },
            actions = {
                // Ícono de perfil clickable a la derecha
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            navController.navigate(Pantalla.RUTA_PERFIL)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = perfil?.name?.firstOrNull()?.uppercase() ?: "U",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
        )
        Divisor()
    }
}



/**
 * Tarjeta individual de notificación.
 *
 * Muestra el título, descripción, fecha y un badge de categoría.
 *
 * @param notificacion Objeto [Notif] con los datos de la notificación.
 */
@Composable
private fun NotificationCard(notificacion: NotificacionUI) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // ✅ Título con wrapping
                Text(
                    text = notificacion.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f),
                    // se eliminan maxLines y overflow para permitir salto de línea
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = notificacion.fecha,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF8F8F8F)),
                    textAlign = TextAlign.End
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = notificacion.descripcion,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5B5B5B)),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
