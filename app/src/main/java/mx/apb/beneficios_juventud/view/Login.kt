package mx.apb.beneficios_juventud.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// App principal
@Composable
fun Login(loginClick: () -> Unit, modifier: Modifier = Modifier) {
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
                valor = "",
                onCambio = {})
            CampoContrasena(
                valor = "",
                onCambio = {})
            //OlvidasteContrasena()
            /**
            Button( // TODO hacer que el botón sí valide el correo/celular y contraseña
            onClick = { navController.navigate((Pantalla.RUTA_MAPA)) }
            ) {
            Text("Ingresar")
            }
             */
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
/*
@Composable
fun OlvidasteContrasena(navController: NavController) {
    ClickableText(
        text = AnnotatedString("¿Olvidaste tu contraseña?"),
        onClick = { navController.navigate(Pantalla.RUTA_OLVIDASTE) },
        style = androidx.compose.ui.text.TextStyle(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )
    )
}
 */
