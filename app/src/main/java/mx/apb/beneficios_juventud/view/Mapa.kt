package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.Box
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
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * @author: Israel González Huerta
 */

@Composable
fun Mapa(beneficiosVM: BeneficiosVM){
    val estado by beneficiosVM.estado.collectAsState()
    Scaffold(
        topBar = {
            TextField(
                value = estado.solicitudMapa,
                onValueChange = { beneficiosVM.actualizarSolicitudMapa(it) },
                placeholder = { Text("Buscar en la zona") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ){ padding ->
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