package mx.apb.beneficios_juventud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.apb.beneficios_juventud.ui.theme.Beneficios_juventudTheme
import mx.apb.beneficios_juventud.view.Menu
import mx.apb.beneficios_juventud.view.Pantalla
import mx.apb.beneficios_juventud.viewmodel.BeneficiosVM

/**
 * La pantalla default va a ser el login, por el momento
 * no valida las credenciales con la base de datos.
 * @author: Israel González Huerta
 */

class MainActivity : ComponentActivity() {
    // Viewmodel
    private val viewModel: BeneficiosVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Beneficios_juventudTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        viewModel,
                        navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(beneficiosVM: BeneficiosVM, navController: NavHostController,modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.RUTA_LOGIN,
        modifier = modifier.fillMaxSize()
    ) {
        composable(Pantalla.RUTA_LOGIN) {
            Login(beneficiosVM, navController)
        }
        composable(Pantalla.RUTA_MENU) {
            Menu(beneficiosVM, navController)
        }
    }
}

// App principal
@Composable
fun Login(beneficiosVM: BeneficiosVM, navController: NavHostController, modifier: Modifier = Modifier) {
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
                onCambio = { beneficiosVM.actualizarCorreo(it)})
            CampoContrasena(
                valor = estado.contrasena,
                onCambio = {beneficiosVM.actualizarContrasena(it)}
            )
            Button(
                onClick = {navController.navigate((Pantalla.RUTA_MENU)) }
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

/**
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Beneficios_juventudTheme {
        Login()
    }
}
*/