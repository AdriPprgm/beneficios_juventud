package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import mx.apb.beneficios_juventud.model.Notif

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Avisos(navController: NavHostController) {

    val notificaciones = listOf(
        Notif(
            titulo = "¡Six Flags 2x1!",
            descripcion = "Promoción especial para beneficiarios. Válida hasta 31 de diciembre.",
            fecha = "20 Nov 2025",
            badge = "Entretenimiento"
        ),
        Notif(
            titulo = "Farmacias Guadalajara 15% Dcto.",
            descripcion = "Aprovecha el 15% de descuento en medicamentos seleccionados.",
            fecha = "18 Nov 2025",
            badge = "Salud"
        ),
        Notif(
            titulo = "Cinépolis: Entrada al 2x1",
            descripcion = "Disfruta de tus películas favoritas con 2x1 en entradas.",
            fecha = "15 Nov 2025",
            badge = "Entretenimiento"
        ),
        Notif(
            titulo = "Nuevo Beneficio: Little Caesars",
            descripcion = "Pizza grande por solo $99 MXN. ¡No te lo pierdas!",
            fecha = "10 Nov 2025",
            badge = "Comida"
        ),
        Notif(
            titulo = "Sally Beauty: 20% en productos",
            descripcion = "Renueva tu look con este increíble descuento en Sally Beauty.",
            fecha = "05 Nov 2025",
            badge = "Belleza"
        ),
        Notif(
            titulo = "Librerías Gandhi: 10% en libros",
            descripcion = "Fomenta la lectura con descuentos en todas las sucursales Gandhi.",
            fecha = "01 Nov 2025",
            badge = "Educación"
        )
    )

    Scaffold(
        topBar = {
            TopBarAvisos(
                title = "Notificaciones",
                onBack = { navController.popBackStack() }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notificaciones.size) { idx ->
                NotificationCard(notificacion = notificaciones[idx])
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarAvisos(
    title: String,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
            )
        }
    )
}

@Composable
private fun NotificationCard(notificacion: Notif) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Encabezado
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

            Text(
                text = notificacion.descripcion,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5B5B5B)),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))


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