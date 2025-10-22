package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.viewmodel.DetalleEstablecimientoVM
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.apb.beneficios_juventud.model.Promo

// Estilos para la insignia de descuento
private val PurpleBadge = Color(0xFF7C4DFF)
private val BadgeTextColor = Color.White

@Composable
fun CatalogoNegocio(
    navController: NavHostController,
    idEstablecimiento: Int? = null
) {
    val vm: DetalleEstablecimientoVM = viewModel(
        factory = DetalleEstablecimientoVM.factory(ClienteApi.service)
    )
    val promosState by vm.promos.collectAsState()

    LaunchedEffect(idEstablecimiento) {
        idEstablecimiento?.takeIf { it > 0 }?.let { vm.cargarPromociones(it) }
    }

    val headerNombre by remember(promosState.items) {
        mutableStateOf(promosState.items.firstOrNull()?.establecimientoNombre ?: "Negocio")
    }
    val headerLogo by remember(promosState.items) {
        mutableStateOf(promosState.items.firstOrNull()?.establecimientoLogoUrl)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopBarNegocio(
                title = headerNombre,
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        when {
            idEstablecimiento == null || idEstablecimiento <= 0 -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text("Sin establecimiento seleccionado") }
            }
            promosState.cargando -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            promosState.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text("Error: ${promosState.error}") }
            }
            promosState.items.isEmpty() -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text("No hay promociones disponibles") }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    // Banner e información inicial (similar al prototipo)
                    item {
                        BannerNegocio(
                            bannerUrl = headerLogo, // usamos logo grande como banner; si tienes un banner real, cámbialo aquí
                            contentDescription = headerNombre
                        )

                        Text(
                            text = headerNombre,
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
                    items(promosState.items) { promo ->
                        PromoCardNetwork(
                            promo = promo,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

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
            // Avatar/placeholder
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
private fun BannerNegocio(
    bannerUrl: String?,
    contentDescription: String
) {
    AsyncImage(
        model = bannerUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentScale = ContentScale.Crop
    )
}

/**
 * Tarjeta de promoción que consume datos de la API (Promocion de dominio).
 * Incluye imagen, título, descripción y un badge (descuento o vigencia).
 */
@Composable
private fun PromoCardNetwork(
    promo: Promo,
    modifier: Modifier = Modifier
) {
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
            // Imagen de la promo (si no hay, queda vacío sin crashear)
            AsyncImage(
                model = promo.imagenUrl,
                contentDescription = promo.titulo,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = promo.titulo,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f)
                    )

                    // Badge dinámico: intenta mostrar descuento, si no hay, muestra vigencia
                    val badgeText = remember(promo) { promo.toBadgeText() }
                    if (badgeText != null) {
                        BadgePill(text = badgeText)
                    }
                }

                Spacer(Modifier.height(6.dp))

                if (!promo.descripcion.isNullOrBlank()) {
                    Text(
                        text = promo.descripcion!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF616161)
                    )
                }
            }
        }
    }
}

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

/**
 * Deriva un texto para el badge a partir de los campos disponibles.
 * Si tienes `discountType/discountValue` en tu modelo de dominio,
 * ajusta este helper para reflejar porcentaje/monto.
 */
private fun Promo.toBadgeText(): String? {
    return if (vigente) "Vigente" else "No vigente"
}


