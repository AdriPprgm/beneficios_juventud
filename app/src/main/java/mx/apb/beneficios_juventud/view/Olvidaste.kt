package mx.apb.beneficios_juventud.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import androidx.compose.ui.Modifier

@Composable
fun Olvidaste(beneficiosVM: BeneficiosVM, navController: NavController, modifier: Modifier = Modifier)
{
    Text(text = "Olvidaste",
        style = MaterialTheme.typography.headlineMedium)
}