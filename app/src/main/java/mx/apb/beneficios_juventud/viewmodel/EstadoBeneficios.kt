package mx.apb.beneficios_juventud.viewmodel

import mx.apb.beneficios_juventud.model.PinMapa

data class EstadoBeneficios(
    // Variables para login
    var contrasena: String = "",
    var credencial: String = "",

    var expiredSess: Boolean = false,

    var verifyingSess: Boolean = true,
    var loginSuccess: Boolean = false,
    var LoadingLogin: Boolean = false,

    var mensajeError: String = "", // DESPUES AGREGAMOS FUNCIONALIDAD A ESTE VALOR

    // Variables para el mapa
    val solicitudMapa: String = "",
    val pinsMapa: List<PinMapa> = emptyList(),
    val cargandoMapa: Boolean = false,
    val errorMapa: String? = null
)
