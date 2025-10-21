package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.R

/**
 * Pantalla que muestra la tarjeta digital del beneficiario con su QR.
 *
 * Navegación: se accede desde el botón "Tarjeta digital" en Perfil.
 *
 * Requisitos:
 *  - Asegúrate de tener en /res/drawable el archivo: qr_beneficios.png
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaDigital(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tarjeta digital") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
        ) {
            Text(
                text = "Muestra este código en los establecimientos afiliados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Si usas un QR dinámico más adelante, aquí podrías reemplazar la Image.
                    Image(
                        painter = painterResource(id = R.drawable.qr_beneficios),
                        contentDescription = "Código QR de beneficios",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)   // Mantiene el QR cuadrado
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Asegúrate de que el QR sea legible y tenga buena iluminación.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}
