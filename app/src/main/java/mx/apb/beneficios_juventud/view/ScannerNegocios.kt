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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import mx.apb.beneficios_juventud.viewmodel.ScannerVM
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerNegocios(navController: NavController,
                    modelo: BeneficiosVM) {
    val currentRoute = Pantalla.RUTA_SCANNER_NEGOCIOS
    var resultado by remember { mutableStateOf("") }
    var tienePermisoCamara by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scannerVM: ScannerVM = viewModel() // Instanciamos el ViewModel
    val scope = rememberCoroutineScope()   // Para lanzar corutinas desde Compose

    val pedirPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        tienePermisoCamara = isGranted
    }

    LaunchedEffect(Unit) {
        val permisoActual = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
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
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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
        Box(modifier = Modifier.padding(innerPadding), contentAlignment = Alignment.Center) {
            when {
                tienePermisoCamara -> {
                    ScannerView { qrValue ->
                        resultado = qrValue
                    }
                }
                else -> {
                    Text(
                        text = "Se necesita permiso de cámara para escanear códigos",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            var navegacionRealizada by remember { mutableStateOf(false) }

            LaunchedEffect(resultado) {
                if (resultado.isNotEmpty() && !navegacionRealizada) {
                    try {
                        val datos = JSONObject(resultado)
                        val userId = datos.getInt("userId").toString()
                        val idPromocion = datos.getString("idPromocion")
                        val timestamp = datos.getLong("timestamp").toString()
                        val expirationTime = datos.getLong("expirationTime").toString()


                        println("ID Usuario: $userId, ID Promoción: $idPromocion, Timestamp: $timestamp, Expira: $expirationTime")

                        // Actualizamos el VM
                        scannerVM.actualizarDatosQR(
                            // userId = userId.toString(),
                            userId = userId,
                            idPromocion = idPromocion,
                            // timestamp = timestamp.toString(),
                            timestamp = timestamp,
                            // expirationTime = expirationTime.toString(),
                            expirationTime = expirationTime,
                            idDueno = modelo.obtenerId().toString()
                        )

                        // Llamamos a Scaneo y mostramos el status en Logcat
                        scope.launch {
                            scannerVM.Scaneo() // Esto imprimirá ✅ o ⚠️ según el response
                        }

                        navController.navigate(Pantalla.RUTA_REGISTROS_NEGOCIOS)
                        navegacionRealizada = true

                    } catch (e: Exception) {
                        println("No se pudo parsear el QR: ${e.message}")
                    }
                }
            }
        }
    }
}

