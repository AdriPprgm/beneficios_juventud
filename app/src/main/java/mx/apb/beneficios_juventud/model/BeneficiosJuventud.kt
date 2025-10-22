package mx.apb.beneficios_juventud.model

/**
 * Modelo de sesión del usuario autenticado.
 *
 * Contiene los datos principales del usuario y el token JWT
 * retornado por el backend después del login.
 */
class BeneficiosJuventud {

    // ---------------------------
    // Datos básicos del usuario
    // ---------------------------
    var correo: String = ""
        private set

    var celular: String = ""
        private set

    var nombre: String? = null
        private set

    var folio: String? = null
        private set

    var rol: String? = null
        private set

    var id: Int? = null
        private set


    // ---------------------------
    // Autenticación
    // ---------------------------
    var token: String? = null
        private set


    // ---------------------------
    // Métodos públicos seguros
    // ---------------------------

    /** Guarda el token JWT recibido desde el backend */
    fun setToken(nuevoToken: String?) {
        token = nuevoToken
    }

    /** Guarda el correo electrónico del usuario */
    fun setCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
    }

    /** Guarda el número de celular del usuario */
    fun setCelular(nuevoCelular: String) {
        celular = nuevoCelular
    }

    /** Guarda el rol (beneficiario, dueno, admin) */
    fun setRol(nuevoRol: String?) {
        rol = nuevoRol
    }

    /** Guarda el folio (solo para beneficiarios) */
    fun setFolio(nuevoFolio: String?) {
        folio = nuevoFolio
    }

    /** Guarda el nombre completo del usuario */
    fun setNombre(nuevoNombre: String?) {
        nombre = nuevoNombre
    }

    fun setId(nuevoId: Int?) {
        id = nuevoId
    }

    /** Limpia todos los datos al cerrar sesión */
    fun limpiarDatos() {
        correo = ""
        celular = ""
        token = null
        nombre = null
        folio = null
        rol = null
        id = null
    }

    override fun toString(): String {
        return "Usuario(correo=$correo, rol=$rol, nombre=$nombre, folio=$folio, token=${token?.take(15)}...)"
    }
}

