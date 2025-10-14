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

        //  Configuración del mapa
        val sixflags = LatLng(19.2953821, -99.2097713702751)
        val labtk = LatLng(19.55407429127162, -99.24117214324389)
        val galerias = LatLng(19.54937295, -99.2744741764268)
        val teatro = LatLng(19.57203264749966, -99.23721587983601)
        val costco = LatLng(19.548420551297493, -99.27081424856226)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(labtk, 12f)
        }

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
                // Lista de establecimientos marcados
                Marker(
                    state = MarkerState(position = labtk),
                    title = "La BTK Atizapán",
                    snippet = "¡2 x 1 para estudiantes!"
                )

                Marker(
                    state = MarkerState(position = sixflags),
                    title = "Six Flags",
                    snippet = "Descuento en pase anual"
                )

                Marker(
                    state = MarkerState(position = galerias),
                    title = "Galerías Atizapán",
                    snippet = "10% de descuento en Starbucks"
                )

                Marker(
                    state = MarkerState(position = teatro),
                    title = "Teatro Zaragoza",
                    snippet = "Descuentos en obras seleccionadas"
                )

                Marker(
                    state = MarkerState(position = costco),
                    title = "Costco Atizapán",
                    snippet = "Descuentos en productos seleccionados"
                )
            }
        }
    }
}

