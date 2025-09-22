package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

// App principal
@Composable
fun Login(loginClick: () -> Unit, beneficiosVM: BeneficiosVM, navController: NavHostController, modifier: Modifier = Modifier) {
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
            Titulo("Iniciar sesión")
            CampoIdentificador(
                valor = estado.correo,
                onCambio = { beneficiosVM.actualizarCorreo(it) })
            CampoContrasena(
                valor = estado.contrasena,
                onCambio = { beneficiosVM.actualizarContrasena(it) })
            OlvidasteContrasena(navController)
            // TODO hacer que el botón sí valide el correo/celular y contraseña
            Button(
                onClick = loginClick
            ) {
                Text("Ingresar")
            }
        }
    }
}


@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto,
        style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun CampoIdentificador(valor: String, onCambio: (String) -> Unit) {
    OutlinedTextField(
        value = valor, // Obtener el valor del estado (a través del VM)
        onValueChange = onCambio, // Pasar el valor al VM
        label = { Text("Correo electrónico o número de celular") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CampoContrasena(valor: String, onCambio: (String) -> Unit) {
    OutlinedTextField(
        value = valor, // Obtener el valor del estado (a través del VM)
        onValueChange = onCambio, // Pasar el valor al VM
        label = { Text("Contraseña") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OlvidasteContrasena(navController: NavHostController) {
    Text(
        text = "¿Olvidaste tu contraseña?",
        color = Color.Blue,
        modifier = Modifier.clickable {
            navController.navigate(Pantalla.RUTA_OLVIDASTE)
        }
    )
}

