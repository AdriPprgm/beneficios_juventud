package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM


// App principal
@Composable
fun Menu(beneficiosVM: BeneficiosVM, navController: NavController, modifier: Modifier = Modifier) {
    val estado by beneficiosVM.estado.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Titulo("Menu")
        }
    }
}


@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto,
        style = MaterialTheme.typography.headlineMedium)
}