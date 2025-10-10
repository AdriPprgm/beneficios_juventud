package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * @author: Israel González Huerta
 */

@Composable
fun Mapa(beneficiosVM: BeneficiosVM) {
    val estado by beneficiosVM.estado.collectAsState()
    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Mapa")
        }
    }
}
