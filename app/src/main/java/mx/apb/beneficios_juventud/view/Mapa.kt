package mx.apb.beneficios_juventud.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * @author: Israel González Huerta
 * @author: Juan Pablo Solis Gomez
 */

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun Mapa(beneficiosVM: BeneficiosVM, navController: NavHostController) {
    val estado by beneficiosVM.estado.collectAsState()

    // Marcadores de negocios TODO pasar al VM
    data class Pin(val titulo: String, val snippet: String, val pos: LatLng)

    val sixflags = LatLng(19.2953821, -99.2097713702751)
    val labtk = LatLng(19.55407429127162, -99.24117214324389)
    val galerias = LatLng(19.54937295, -99.2744741764268)
    val teatro = LatLng(19.57203264749966, -99.23721587983601)
    val costco = LatLng(19.548420551297493, -99.27081424856226)

    val pins = remember {
        listOf(
            Pin("La BTK Atizapán", "¡2 x 1 para estudiantes!", labtk),
            Pin("Six Flags", "Descuento en pase anual", sixflags),
            Pin("Galerías Atizapán", "10% de descuento en Starbucks", galerias),
            Pin("Teatro Zaragoza", "Descuentos en obras seleccionadas", teatro),
            Pin("Costco Atizapán", "Descuentos en productos seleccionados", costco)
        )
    }

    // Filtrado
    fun norm(s: String) = java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()

    val query = estado.solicitudMapa
    val sugerencias = remember(query, pins) {
        val q = norm(query)
        val base = if (q.isBlank()) pins else pins.filter {
            norm(it.titulo).contains(q) || norm(it.snippet).contains(q)
        }
        base.take(6) // limite sugerencias
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(labtk, 12f)
    }
    val scope = rememberCoroutineScope()


    // Punto azul
    val context = LocalContext.current
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    val properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
    val uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = true)

    // Estado del dropdown de sugerencias
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 16.dp, 8.dp, 0.dp)
            ) {
                // Fila superior: título centrado + ícono a la derecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    // Título centrado
                    Text(
                        text = "Mapa de establecimientos",
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Ícono de perfil clickable (a la derecha)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .clickable {
                                navController.navigate(Pantalla.RUTA_PERFIL)
                            }
                    )
                }

                // Campo de búsqueda con autocompletar
                ExposedDropdownMenuBox(
                    expanded = expanded && sugerencias.isNotEmpty(),
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = estado.solicitudMapa,
                        onValueChange = { texto ->
                            beneficiosVM.actualizarSolicitudMapa(texto)
                        },
                        placeholder = { Text("Buscar por nombre o beneficio…") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(top = 8.dp),
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                expanded = false
                                focusManager.clearFocus()
                            }
                        ),
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    // Lista desplegable con sugerencias
                    ExposedDropdownMenu(
                        expanded = expanded && sugerencias.isNotEmpty(),
                        onDismissRequest = { expanded = false }
                    ) {
                        sugerencias.forEach { s ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(s.titulo, style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            s.snippet,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                },
                                onClick = {
                                    beneficiosVM.actualizarSolicitudMapa(s.titulo)
                                    expanded = false
                                    focusManager.clearFocus()

                                    // Mover la cámara al marcador
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(s.pos, 16f), 550
                                        )
                                    }
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                // Pinta solo los resultados para que coincida con el filtro
                val visibles = if (query.isBlank()) pins else sugerencias
                visibles.forEach { p ->
                    Marker(
                        state = MarkerState(p.pos),
                        title = p.titulo,
                        snippet = p.snippet
                    )
                }
            }
        }
    }
}



