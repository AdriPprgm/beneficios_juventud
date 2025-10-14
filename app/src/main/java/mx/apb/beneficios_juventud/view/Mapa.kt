package mx.apb.beneficios_juventud.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * @author: Israel González Huerta & Juan Pablo Solis Gomez
 */

@SuppressLint("UnrememberedMutableState")
@Composable
fun Mapa(beneficiosVM: BeneficiosVM) {
    val estado by beneficiosVM.estado.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Mapa de establecimientos",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = estado.solicitudMapa,
                    onValueChange = { beneficiosVM.actualizarSolicitudMapa(it) },
                    placeholder = { Text("Buscar en la zona") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    ) { padding ->

        // --- Configuración del mapa ---
        val atizapan = LatLng(19.56, -99.25)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(atizapan, 12f)
        }

        // Punto azul (si ya tienes permiso en tiempo de ejecución)
        val context = LocalContext.current
        val hasLocationPermission = remember {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }

        val properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        )
        val uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                // Marcador base (puedes luego mapear tu lista de establecimientos aquí)
                Marker(
                    state = MarkerState(position = atizapan),
                    title = "Atizapán",
                    snippet = "Beneficios Juventud"
                )
            }
        }
    }
}

