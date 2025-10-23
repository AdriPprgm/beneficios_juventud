package mx.apb.beneficios_juventud.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import mx.apb.beneficios_juventud.viewmodel.ForgotUiState

/**
 * Pantalla genérica para "Olvidaste tu contraseña"
 * Sin funcionalidad, solo interfaz.
 * @author: Israel González Huerta
 */
@Composable
fun Olvidaste(beneficiosVM: BeneficiosVM, navController: NavController, modifier: Modifier = Modifier)
{
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val forgotState by beneficiosVM.forgotState.collectAsState()


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
            // Logo de Atizapán de Zaragoza
            AsyncImage(
                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0SU-5o1tEX9nEWacSjibNe2wy_Dp9TmDhzg&s",
                contentDescription = "Logo beneficios juventud",
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Título
            Titulo("¿Olvidaste tu contraseña?")

            // Descripción
            Text(
                text = "Ingresa tu correo electrónico y te enviaremos instrucciones para restablecer tu contraseña.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de correo
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de enviar instrucciones
            Button(
                onClick = { beneficiosVM.enviarCorreoReset(email.value.text) },
                enabled = forgotState !is ForgotUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Enviar instrucciones")
            }

            when (val st = forgotState) {
                is ForgotUiState.Success -> Text(
                    text = st.msg,
                    color = Color(0xFF2E7D32)
                )
                is ForgotUiState.Error -> Text(
                    text = st.msg,
                    color = MaterialTheme.colorScheme.error
                )
                else -> {}
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Texto para volver atrás
            RegresarALogin(navController)
        }
    }
}

@Composable
fun RegresarALogin(navController: NavController) {
    Text(
        text = "Volver al inicio de sesión",
        color = Color.Blue,
        modifier = Modifier.clickable {
            navController.navigate(Pantalla.RUTA_LOGIN)
        }
    )
}