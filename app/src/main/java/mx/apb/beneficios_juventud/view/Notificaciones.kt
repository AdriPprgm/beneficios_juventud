package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.model.Notificacion
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * Composable principal que muestra la lista de notificaciones del usuario.
 * @author Juan Pablo Solís Gómez
 *
 * @param navController Controlador de navegación para regresar a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Avisos(navController: NavHostController, beneficiosVM: BeneficiosVM) {

    val notificaciones = listOf(
        Notificacion("¡Six Flags 2x1!", "Promoción especial para beneficiarios. Válida hasta 31 de diciembre.", "20 Nov 2025", "Entretenimiento"),
        Notificacion("Farmacias Guadalajara 15% Dcto.", "Aprovecha el 15% de descuento en medicamentos seleccionados.", "18 Nov 2025", "Salud"),
        Notificacion("Cinépolis: Entrada al 2x1", "Disfruta de tus películas favoritas con 2x1 en entradas.", "15 Nov 2025", "Entretenimiento"),
        Notificacion("Nuevo Beneficio: Little Caesars", "Pizza grande por solo $99 MXN. ¡No te lo pierdas!", "10 Nov 2025", "Comida"),
        Notificacion("Sally Beauty: 20% en productos", "Renueva tu look con este increíble descuento en Sally Beauty.", "05 Nov 2025", "Belleza"),
        Notificacion("Librerías Gandhi: 10% en libros", "Fomenta la lectura con descuentos en todas las sucursales Gandhi.", "01 Nov 2025", "Educación")
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarAvisos(
                title = "Notificaciones",
                navController = navController
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
            items(notificaciones.size) { idx ->
                NotificationCard(notificacion = notificaciones[idx])
            }
            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        ClienteApi.actualizarToken(null)
                        beneficiosVM.signOut()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text("Cerrar sesión")
                }
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
    navController: NavHostController
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
                // Ícono de perfil clickable
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                        .clickable {
                            navController.navigate(Pantalla.RUTA_PERFIL)
                        }
                )
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
private fun NotificationCard(notificacion: Notificacion) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Encabezado con título y fecha
            Row(verticalAlignment = Alignment.CenterVertically) {

                Spacer(Modifier.width(10.dp))

                Text(
                    text = notificacion.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = notificacion.fecha,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF8F8F8F)),
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(8.dp))

            // Descripción de la notificación
            Text(
                text = notificacion.descripcion,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5B5B5B)),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            // Badge con categoría de la notificación
            AssistChip(
                onClick = {  },
                label = { Text(notificacion.badge, style = MaterialTheme.typography.labelMedium) },
                shape = RoundedCornerShape(percent = 50),
                border = BorderStroke(1.dp, Color(0xFFB39DDB)),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFF6F2FF),
                    labelColor = Color(0xFF6A1B9A)
                )
            )
        }
    }
}


