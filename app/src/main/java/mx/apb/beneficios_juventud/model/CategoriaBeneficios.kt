package mx.apb.beneficios_juventud.model


/**
 * Enumeración que representa las categorías disponibles de beneficios
 * dentro del programa **Beneficios Juventud**.
 *
 * Cada categoría se asocia con una etiqueta legible para mostrar en la interfaz de usuario,
 * y permite clasificar los establecimientos o promociones de acuerdo con su tipo.
 *
 * Esta enumeración se utiliza para filtrar, mostrar o registrar los beneficios
 * según su área temática principal (entretenimiento, salud o educación).
 */
enum class CategoriaBeneficios(val label: String) {
    ENTRETENIMIENTO("Entretenimiento"),
    SALUD("Salud"),
    EDUCACION("Educación")
}