package mx.apb.beneficios_juventud.model

import mx.apb.beneficios_juventud.R

/**
 * @author Israel González Huerta
 * Clase utilizada por los filtros en la pantalla de menu
 */

data class ClasesMenu(
    val nombre: String,
    val activada: Boolean
)

/**
 * Modelo simple de una oferta
 */
data class Oferta(
    val imagenRes: Int,
    val titulo: String,
    val descripcion: String
)

val ofertas = listOf(
    Oferta(R.drawable.oferta1, "Six Flags", "10% de descuento en pases de un día"),
    Oferta(R.drawable.oferta2, "Starbucks", "2x1 en bebidas de temporada los viernes"),
    Oferta(R.drawable.oferta3, "Cinemex", "Boleto gratis al comprar dos entradas en línea"),
    Oferta(R.drawable.oferta4, "H&M", "20% de descuento en tu primera compra con la app"),
    Oferta(R.drawable.oferta5, "Udemy", "50% de descuento en cursos de programación seleccionados")
    )