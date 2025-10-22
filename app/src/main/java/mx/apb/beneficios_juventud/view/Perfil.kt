package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wc
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.model.BeneficiarioDetalle
import mx.apb.beneficios_juventud.viewmodel.PerfilVM
import mx.apb.beneficios_juventud.viewmodel.EstadoPerfil
import mx.apb.beneficios_juventud.model.CategoriaBeneficios
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.model.UsoBeneficios
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import java.time.format.DateTimeFormatter
import java.util.Locale
import mx.apb.beneficios_juventud.R

/**
 * Pantalla de perfil de usuario que muestra información del usuario y su historial de beneficios.
 *
 * @param navController Controlador de navegación para mover entre pantallas.
 * @param beneficiosVM ViewModel principal que maneja el estado de sesión y login.
 */
@Composable
fun Perfil(
    navController: NavHostController,
    beneficiosVM: BeneficiosVM
) {
    val estado by beneficiosVM.estado.collectAsState()
    val vm: PerfilVM = viewModel()
    val state by vm.uiState.collectAsState()
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Redirige al login si no hay sesión activa
    if (!estado.loginSuccess) {
        navController.navigate(Pantalla.RUTA_LOGIN)
    }

    // Mostrar diálogo de confirmación para cerrar sesión
    if (mostrarConfirmacion) {
        ConfirmarLogout(
            onDismiss = { mostrarConfirmacion = false },
            onConfirm = {
                beneficiosVM.signOut()
                mostrarConfirmacion = false
            }
        )
    }

    Scaffold(
        topBar = { PerfilTopBar() },
        modifier = Modifier.background(Color.White)
    ) { innerPadding ->
        when (val s = state) {
            EstadoPerfil.Loading -> Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is EstadoPerfil.Error -> Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ocurrió un error: ${s.mensaje}")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.reload() }) { Text("Reintentar") }
                }
            }

            is EstadoPerfil.Exito -> {
                val perfil = s.perfil
                val historial = s.historial
                val detalle: mx.apb.beneficios_juventud.model.BeneficiarioDetalle? = null
                PerfilContent(
                    modifier = Modifier.padding(innerPadding),
                    perfil = perfil,
                    historial = historial,
                    detalleBeneficiario = s.detalle,
                    onShowDigitalCard = { navController.navigate(Pantalla.RUTA_TARJETA) },
                    onSignOut = { mostrarConfirmacion = true }
                )
            }
        }
    }
}

/**
 * Barra superior de la pantalla de perfil.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfilTopBar() {
    Column {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Perfil de Usuario",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,          // Fondo blanco
                titleContentColor = Color.Black,       // Texto negro
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )
        Divisor()
    }
}


/**
 * Contenido principal del perfil.
 *
 * @param modifier Modificador de Compose para la columna principal.
 * @param perfil Datos del usuario a mostrar.
 * @param historial Lista de beneficios utilizados por el usuario.
 * @param onSignOut Callback para cerrar sesión.
 */
@Composable
private fun PerfilContent(
    modifier: Modifier = Modifier,
    perfil: PerfilUsuario,
    historial: List<UsoBeneficios>,
    detalleBeneficiario: BeneficiarioDetalle?,
    onShowDigitalCard: () -> Unit,
    onSignOut: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd 'de' LLLL, yyyy") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { ProfileCard(perfil = perfil) }

        item {
            // Mostrar la tarjeta de detalles SOLO si la tenemos
            if (detalleBeneficiario != null) {
                PerfilDetallesCard(detalleBeneficiario)
                Spacer(Modifier.height(8.dp))
            }
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
                onClick = onShowDigitalCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) { Text("Tarjeta digital") }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) { Text("Cerrar sesión") }
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

/**
 * Tarjeta con información del perfil del usuario.
 */
@Composable
private fun ProfileCard(perfil: PerfilUsuario) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            painter = painterResource(id = R.drawable.tarjeta_benefico),
            contentDescription = "Tarjeta Beneficio Joven",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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


/**
 * Elemento de historial de beneficios usados.
 *
 * @param uso Información del beneficio usado.
 * @param formatter Formateador de fechas.
 */
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

/**
 * Chip que representa la categoría del beneficio.
 *
 * @param c Categoría del beneficio.
 */
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

/**
 * Diálogo de confirmación para cerrar sesión.
 *
 * @param onDismiss Callback al cancelar.
 * @param onConfirm Callback al confirmar.
 */
@Composable
fun ConfirmarLogout(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onConfirm) { Text("Cerrar sesión") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Regresar") } },
        title = { Text("Aviso") },
        text = { Text("¿Desea cerrar sesión?") }
    )
}

/**
 * Tarjeta que muestra los detalles completos del beneficiario autenticado.
 *
 * Presenta información obtenida desde la API, incluyendo:
 * - Nombre(s) y apellidos
 * - Fecha de nacimiento
 * - Celular
 * - Folio
 * - Correo electrónico
 * - Sexo
 *
 * Esta vista se genera únicamente si el usuario autenticado tiene el rol
 * de **beneficiario**, y los datos provienen del endpoint `/mobile/detalles-perfil`.
 *
 * @param detalle Objeto [BeneficiarioDetalle] con los datos del beneficiario actual.
 */
@Composable
private fun PerfilDetallesCard(detalle: BeneficiarioDetalle) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Detalles del beneficiario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.Badge,
                label = "Nombre(s)",
                value = listOfNotNull(detalle.primerNombre, detalle.segundoNombre)
                    .filter { !it.isNullOrBlank() }
                    .joinToString(" ")
                    .ifBlank { "—" }
            )

            InfoRow(
                icon = Icons.Default.ContactPage,
                label = "Apellidos",
                value = listOfNotNull(detalle.apellidoPaterno, detalle.apellidoMaterno)
                    .filter { !it.isNullOrBlank() }
                    .joinToString(" ")
                    .ifBlank { "—" }
            )

            InfoRow(
                icon = Icons.Default.Cake,
                label = "Fecha de nacimiento",
                value = formatFecha(detalle.fechaNacimiento)
            )

            InfoRow(
                icon = Icons.Default.Phone,
                label = "Celular",
                value = detalle.celular?.takeIf { it.isNotBlank() } ?: "—"
            )

            // Toggle folio
            var mostrarFolioCompleto by remember { mutableStateOf(false) }
            val folioMostrado = when {
                detalle.folio.isNullOrBlank() -> "—"
                mostrarFolioCompleto -> detalle.folio!!
                else -> maskFolio(detalle.folio)
            }

            FolioRow(detalle.folio)

            InfoRow(
                icon = Icons.Default.Email,
                label = "Correo electrónico",
                value = detalle.email?.takeIf { it.isNotBlank() } ?: "—"
            )

            InfoRow(
                icon = Icons.Default.Wc,
                label = "Sexo",
                value = mapSexo(detalle.sexo)
            )
        }
    }
}
/**
 * Fila composable que muestra el folio del beneficiario con opción para
 * alternar entre su versión enmascarada y la completa.
 *
 * Esta función utiliza un estado local (`mostrarCompleto`) que se reinicia
 * automáticamente cuando el valor del folio cambia, garantizando que la
 * visibilidad del texto siempre esté sincronizada con los datos más recientes.
 *
 * - Por defecto, el folio se muestra enmascarado para proteger la privacidad del usuario.
 * - Al tocar el ícono de "ojo" (`Visibility`), el folio completo se revela temporalmente.
 * - Al volver a presionar, se oculta nuevamente.
 */
@Composable
private fun FolioRow(folio: String?) {
    // Estado local que se reinicia si cambia el folio
    var mostrarCompleto by remember(folio) { mutableStateOf(false) }

    val valor = when {
        folio.isNullOrBlank() -> "—"
        mostrarCompleto -> folio
        else -> maskFolio(folio)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ContactPage,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(10.dp))

        Column(Modifier.weight(1f)) {
            Text(
                "Folio",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(onClick = { mostrarCompleto = !mostrarCompleto }) {
            Icon(
                imageVector = if (mostrarCompleto) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = if (mostrarCompleto) "Ocultar folio" else "Mostrar folio"
            )
        }
    }
}


/**
 * Fila reutilizable para mostrar un campo del perfil junto a un ícono descriptivo.
 *
 * Utilizada en la tarjeta de detalles del beneficiario para presentar pares clave-valor
 * como "Nombre", "Correo", "Folio", etc., con un diseño limpio y alineado.
 *
 * @param icon Ícono representativo del campo.
 * @param label Etiqueta que describe el dato (por ejemplo, “Correo”).
 * @param value Valor correspondiente mostrado al usuario.
 */
@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Da formato legible a una fecha ISO (yyyy-MM-dd) en formato largo en español.
 *
 * Ejemplo de salida: `12 de marzo, 2002`
 *
 * @param isoDate Fecha en formato ISO.
 * @return Cadena legible o "—" si no existe o el formato es inválido.
 */
private fun formatFecha(fechaRaw: String?): String {
    if (fechaRaw.isNullOrBlank()) return "—"
    val locMx = Locale("es", "MX")
    val outFmt = java.time.format.DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", locMx)

    return try {
        // Caso 1: ISO con 'Z' (p.ej. 2004-10-09T00:00:00.000Z)
        val instant = java.time.Instant.parse(fechaRaw)
        instant.atZone(java.time.ZoneOffset.UTC).toLocalDate().format(outFmt)
    } catch (_: Exception) {
        try {
            // Caso 2: ISO con offset (p.ej. 2004-10-09T00:00:00+00:00)
            val odt = java.time.OffsetDateTime.parse(fechaRaw)
            odt.toLocalDate().format(outFmt)
        } catch (_: Exception) {
            try {
                // Caso 3: Solo fecha (p.ej. 2004-10-09)
                val ld = java.time.LocalDate.parse(fechaRaw)
                ld.format(outFmt)
            } catch (_: Exception) {
                // Último recurso: deja el valor crudo
                fechaRaw
            }
        }
    }
}


/**
 * Enmascara parcialmente el folio del beneficiario para preservar la privacidad.
 *
 * Ejemplo:
 * ```
 * folio original: "BEN12345678"
 * salida: "**** **** 5678"
 * ```
 *
 * @param folio Folio original proporcionado por la base de datos.
 * @return Folio enmascarado o "—" si está vacío.
 */
private fun maskFolio(folio: String?): String {
    if (folio.isNullOrBlank()) return "—"
    val last = folio.takeLast(4)
    return "**** **** $last"
}

/**
 * Convierte el valor de sexo devuelto por la API en una descripción legible.
 *
 * Valores aceptados:
 * - "M" → "Masculino"
 * - "F" → "Femenino"
 * - "O" → "Otro"
 *
 * @param sexo Valor textual del sexo devuelto por la API.
 * @return Descripción legible o "—" si es nulo o vacío.
 */
private fun mapSexo(sexo: String?): String =
    when (sexo?.trim()?.uppercase()) {
        "M" -> "Masculino"
        "F" -> "Femenino"
        "O" -> "Otro"
        else -> sexo?.ifBlank { "—" } ?: "—"
    }