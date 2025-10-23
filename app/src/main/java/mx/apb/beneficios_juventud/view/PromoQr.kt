package mx.apb.beneficios_juventud.view

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import mx.apb.beneficios_juventud.model.API.ClienteApi
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import mx.apb.beneficios_juventud.viewmodel.PromoQrUiState
import mx.apb.beneficios_juventud.viewmodel.PromoQrVM
import mx.apb.beneficios_juventud.viewmodel.PromoQrVM.Companion.factoryQr


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoQr(
    idPromocion: Long,
    beneficiosVM: BeneficiosVM,
    navController: NavHostController
) {
    val token by beneficiosVM.authToken.collectAsState()
    val vm: PromoQrVM = viewModel(
        factory = factoryQr(
            api = ClienteApi.service,
            tokenProvider = { token ?: "" }
        )
    )

    val state by vm.state.collectAsState()

    LaunchedEffect(idPromocion) {
        vm.cargarQr(idPromocion)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Código QR de promoción") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                })
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    ErrorView(
                        mensaje = state.error ?: "Error",
                        onRetry = { vm.regenerar(idPromocion) }
                    )
                }
                state.qr != null -> {
                    QrContent(state = state, onRegenerar = { vm.regenerar(idPromocion) })
                }
            }
        }
    }
}

@Composable
private fun QrImageFromDataUrl(dataUrl: String, sizeDp: Dp = 280.dp) {
    // Extrae la porción Base64 del Data URL: "data:image/png;base64,XXXX"
    val bytes = remember(dataUrl) {
        val comma = dataUrl.indexOf(',')
        if (comma != -1) {
            val base64 = dataUrl.substring(comma + 1)
            try {
                Base64.decode(base64, Base64.DEFAULT)
            } catch (_: IllegalArgumentException) {
                null
            }
        } else null
    }

    if (bytes != null) {
        val bmp = remember(bytes) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        if (bmp != null) {
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = "Código QR",
                modifier = Modifier
                    .size(sizeDp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            Text("No se pudo decodificar el QR")
        }
    } else {
        Text("Formato de QR inválido")
    }
}

@Composable
private fun QrContent(state: PromoQrUiState, onRegenerar: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = if (state.remainingSec > 0)
                "Expira en ${state.remainingSec}s"
            else
                "QR expirado",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        val dataUrl = state.qr?.qrCodeDataUrl
        if (dataUrl != null) {
            QrImageFromDataUrl(dataUrl, sizeDp = 280.dp)
        } else {
            Text("Generando QR…")
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(
                enabled = state.remainingSec == 0L,
                onClick = onRegenerar
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Regenerar")
            }
        }

        Text(
            "Muéstralo al operador para escanear. El QR es de uso único y dura 5 minutos.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorView(mensaje: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(mensaje)
        Button(onClick = onRetry) { Text("Reintentar") }
    }
}

