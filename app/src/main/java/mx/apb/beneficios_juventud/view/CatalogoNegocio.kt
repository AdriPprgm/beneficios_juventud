package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.R
import mx.apb.beneficios_juventud.model.Promo


// Estilos para la insignia de descuento
private val PurpleBadge = Color(0xFF7C4DFF)
private val BadgeTextColor = Color.White

/**
 * Pantalla de catálogo de un negocio (Six Flags México) que muestra las promociones activas.
 * @author Juan Pablo Solís Gómez
 *
 * @param navController controlador de navegación para volver a la pantalla anterior.
 */
@Composable
fun CatalogoNegocio(navController: NavHostController) {
    val promos = listOf(
        Promo(
            imagenRes = R.drawable.pase_anual,
            titulo = "Pase Anual 2x1",
            descripcion = "Disfruta el doble de diversión con nuestro pase anual exclusivo para beneficiarios.",
            badge = "-50%"
        ),
        Promo(
            imagenRes = R.drawable.comida_combo,
            titulo = "Comida Combo Familiar",
            descripcion = "Combina tu experiencia con descuentos en alimentos y bebidas dentro del parque.",
            badge = "-30%"
        ),
        Promo(
            imagenRes = R.drawable.souvenirs,
            titulo = "15% en Tienda de Souvenirs",
            descripcion = "Lleva un recuerdo inolvidable de tu visita con descuento en la tienda oficial.",
            badge = "-15%"
        )
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarNegocio(
                title = "Six Flags México",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Banner e información inicial
            item {
                Image(
                    painter = painterResource(id = R.drawable.banner_sixflags),
                    contentDescription = "Banner Six Flags",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Six Flags México",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                Text(
                    text = "Promociones Activas",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Lista de promociones
            items(promos) { promo ->
                PromoCard(
                    promo = promo,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * Barra superior con título, botón de regresar y avatar.
 *
 * @param title título mostrado en la barra.
 * @param onBack acción al presionar la flecha de regreso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarNegocio(
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

/**
 * Tarjeta individual que representa una promoción.
 *
 * @param promo promoción a mostrar.
 * @param modifier modificador opcional para personalizar el layout.
 */
@Composable
private fun PromoCard(promo: Promo, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la promoción
            Image(
                painter = painterResource(id = promo.imagenRes),
                contentDescription = promo.titulo,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = promo.titulo,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f)
                    )

                    BadgePill(text = promo.badge)
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = promo.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

/**
 * Indicador visual para mostrar el descuento o badge de la promoción.
 *
 * @param text texto que se mostrará dentro del badge.
 */
@Composable
private fun BadgePill(text: String) {
    Box(
        modifier = Modifier
            .background(PurpleBadge, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = BadgeTextColor,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}
