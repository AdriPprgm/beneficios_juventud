package mx.apb.beneficios_juventud.viewmodel

data class EstadoBeneficios(
    // Variables para login
    var contrasena: String = "",
    var credencial: String = "",

    var loginSuccess: Boolean = false,

    var mensajeError: String = "", // DESPUES AGREGAMOS FUNCIONALIDAD A ESTE VALOR

    // Variables para el mapa
    var solicitudMapa: String = ""
)
