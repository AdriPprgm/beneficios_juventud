package mx.apb.beneficios_juventud.model

/**
 * Modelo de datos que representa la información completa de un beneficiario
 * autenticado dentro del sistema **Beneficios Juventud**.
 *
 * Este modelo se utiliza principalmente en la pantalla de perfil para mostrar
 * los datos personales recuperados desde el endpoint `/mobile/detalles-perfil`.
 *
 * Contiene los campos básicos registrados en la base de datos del beneficiario,
 * y todos son opcionales para manejar posibles valores nulos devueltos por la API.
 */

data class BeneficiarioDetalle(
    val primerNombre: String? = null,
    val segundoNombre: String? = null,
    val apellidoPaterno: String? = null,
    val apellidoMaterno: String? = null,
    val fechaNacimiento: String? = null,
    val celular: String? = null,
    val folio: String? = null,
    val email: String? = null,
    val sexo: String? = null
)