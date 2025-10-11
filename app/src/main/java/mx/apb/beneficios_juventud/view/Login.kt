package mx.apb.beneficios_juventud.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * Pantalla de inicio de sesión para usuarios generales.
 *
 * @param loginClick función que se ejecuta al presionar el botón "Ingresar".
 * @param beneficiosVM instancia del ViewModel para manejar el estado y la lógica de login.
 * @param navController controlador de navegación para moverse entre pantallas.
 * @param modifier modificador opcional para personalizar la UI.
 */
@Composable
fun Login(
    loginClick: () -> Unit,
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

            Titulo("Iniciar sesión")

            CampoIdentificador(
                valor = estado.credencial,
                onCambio = { beneficiosVM.actualizarCredencial(it) }
            )

            CampoContrasena(
                valor = estado.contrasena,
                onCambio = { beneficiosVM.actualizarContrasena(it) }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = loginClick) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(50.dp))

            OlvidasteContrasena(navController)

            Spacer(modifier = Modifier.height(30.dp))

            AccesoNegocio(navController)
        }
    }
}

/**
 * Composable para mostrar un título centrado en la pantalla.
 *
 * @param texto texto que se mostrará.
 * @param modifier modificador opcional para personalizar la UI.
 */
@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Campo de texto para ingresar correo electrónico o número de celular.
 *
 * @param valor valor actual del campo.
 * @param onCambio callback que recibe el nuevo valor del campo.
 */
@Composable
fun CampoIdentificador(valor: String, onCambio: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text("Correo electrónico o número de celular") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Campo de texto para ingresar contraseña.
 *
 * @param valor valor actual del campo.
 * @param onCambio callback que recibe el nuevo valor del campo.
 */
@Composable
fun CampoContrasena(valor: String, onCambio: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text("Contraseña") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Texto interactivo para recuperar la contraseña olvidada.
 *
 * @param navController controlador de navegación para moverse a la pantalla de recuperación.
 */
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

/**
 * Texto interactivo para acceder al login de negocios.
 *
 * @param navController controlador de navegación para moverse a la pantalla de login de negocios.
 */
@Composable
fun AccesoNegocio(navController: NavHostController) {
    Text(
        text = "¿Eres un negocio?",
        color = Color.Blue,
        modifier = Modifier.clickable {
            navController.navigate(Pantalla.RUTA_LOGIN_NEGOCIOS)
        }
    )
}
