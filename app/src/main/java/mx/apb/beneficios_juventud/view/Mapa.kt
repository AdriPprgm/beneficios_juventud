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
import androidx.lifecycle.viewmodel.compose.viewModel
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
import mx.apb.beneficios_juventud.model.PerfilUsuario
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import mx.apb.beneficios_juventud.viewmodel.MenuVM

/**
 * @author:
 *  - Israel González Huerta
 *  - Juan Pablo Solis Gomez
 */

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun Mapa(beneficiosVM: BeneficiosVM, navController: NavHostController, vm: MenuVM = viewModel(), perfil: PerfilUsuario?) {
    val estado by beneficiosVM.estado.collectAsState()

    // Cargar sucursales al entrar
    LaunchedEffect(Unit) { beneficiosVM.cargarSucursales() }

    // Normalizador para filtro
    fun norm(s: String) = java.text.Normalizer.normalize(s.trim(), java.text.Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase()

    val query = estado.solicitudMapa
    val sugerencias = remember(query, estado.pinsMapa) {
        val q = norm(query)
        val base = if (q.isBlank()) estado.pinsMapa else estado.pinsMapa.filter {
            norm(it.titulo).contains(q) || norm(it.snippet).contains(q)
        }
        base.take(6)
    }

    // Centro inicial: primer pin cargado o fallback (Atizapán)
    val fallbackCenter = LatLng(19.55407429127162, -99.24117214324389)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            estado.pinsMapa.firstOrNull()?.pos ?: fallbackCenter, 12f
        )
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
                // Título centrado + avatar/perfil a la derecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Mapa de establecimientos",
                        fontSize = 20.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Ícono de perfil clickable a la derecha
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 2.dp)
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
                }

                // Buscador con sugerencias
                ExposedDropdownMenuBox(
                    expanded = expanded && sugerencias.isNotEmpty(),
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = estado.solicitudMapa,
                        onValueChange = { texto -> beneficiosVM.actualizarSolicitudMapa(texto) },
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
            // Indicadores de estado del mapa (opcionales)
            if (estado.cargandoMapa) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp)
                )
            }
            estado.errorMapa?.let { msg ->
                Text(
                    text = "Error cargando sucursales: $msg",
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 48.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(Color(0x80FFFFFF))
                    .padding(6.dp)
            ) {
                Text("pins: ${estado.pinsMapa.size}", fontSize = 12.sp, color = Color.Black)
                estado.errorMapa?.let { Text("err: $it", fontSize = 12.sp, color = Color.Red) }
            }


            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                val visibles = if (query.isBlank()) estado.pinsMapa else sugerencias
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





