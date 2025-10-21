package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * Pantalla de inicio de sesión para negocios asociados.
 *
 * @param beneficiosVM instancia del ViewModel para manejar el estado y la lógica de login.
 * @param navController controlador de navegación para moverse entre pantallas.
 * @param modifier modificador opcional para personalizar la UI.
 */

@Composable
fun LoginNegocios(
    beneficiosVM: BeneficiosVM,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val estado by beneficiosVM.estado.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Logo de la aplicación
            AsyncImage(
                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0SU-5o1tEX9nEWacSjibNe2wy_Dp9TmDhzg&s",
                contentDescription = "Logo beneficios juventud",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Titulo("Inicio de sesión de negocios asociados")

            CampoIdentificador(
                valor = estado.credencial,
                onCambio = { beneficiosVM.actualizarCredencial(it) }
            )

            CampoContrasena(
                valor = estado.contrasena,
                onCambio = { beneficiosVM.actualizarContrasena(it) }
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Botón de ingreso a la pantalla principal de negocios
            Button(onClick = {
                navController.navigate("MenuNegocios")
            }) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(50.dp))

            OlvidasteContrasena(navController)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
