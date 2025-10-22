package mx.apb.beneficios_juventud.view

import ScannerView
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerNegocios(navController: NavController) {
    val currentRoute = Pantalla.RUTA_SCANNER_NEGOCIOS
    var resultado by remember { mutableStateOf("") }
    var tienePermisoCamara by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Launcher para pedir el permiso de cámara
    val pedirPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        tienePermisoCamara = isGranted
    }

    // Verificar y solicitar permiso al abrir la pantalla
    LaunchedEffect(Unit) {
        val permisoActual = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permisoActual == PackageManager.PERMISSION_GRANTED) {
            tienePermisoCamara = true
        } else {
            pedirPermisoCamara.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Escanear código", fontSize = 20.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )
                Divisor()
            }
        },
        bottomBar = {
            BottomBarNegocios(navController, currentRoute)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                tienePermisoCamara -> {
                    // ✅ Mostrar cámara si ya hay permiso
                    ScannerView { qrValue ->
                        resultado = qrValue
                    }
                }

                else -> {
                    // Mostrar mensaje mientras no hay permiso
                    Text(
                        text = "Se necesita permiso de cámara para escanear códigos",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Si se detectó un QR, lo procesamos
            if (resultado.isNotEmpty()) {
                try {
                    val datos = JSONObject(resultado)
                    val userId = datos.getInt("userId")
                    val idPromocion = datos.getString("idPromocion")
                    val timestamp = datos.getLong("timestamp")
                    val expirationTime = datos.getLong("expirationTime")

                    println("ID Usuario: $userId, ID Promoción: $idPromocion, Timestamp: $timestamp, Expira: $expirationTime")
                } catch (e: Exception) {
                    println("No se pudo parsear el QR: ${e.message}")
                }
            }
        }
    }
}
