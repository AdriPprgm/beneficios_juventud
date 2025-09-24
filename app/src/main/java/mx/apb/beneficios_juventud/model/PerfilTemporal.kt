package mx.apb.beneficios_juventud.model

import java.time.LocalDate

class PerfilTemporal {

    suspend fun getPerfil(): PerfilUsuario =
        PerfilUsuario(
            name = "Adrian Proaño",
            age = 22,
            photoUrl = null
        )

    suspend fun getHistorial(): List<UsoBeneficios> = listOf(
        UsoBeneficios(
            id = "1",
            merchant = "Cinépolis",
            date = LocalDate.now(),
            description = "2x1 en entradas para funciones 2D y 3D",
            category = CategoriaBeneficios.ENTRETENIMIENTO
        ),
        UsoBeneficios(
            id = "2",
            merchant = "Farmacias Guadalajara",
            date = LocalDate.now(),
            description = "15% de descuento en productos de higiene personal",
            category = CategoriaBeneficios.SALUD
        ),
        UsoBeneficios(
            id = "3",
            merchant = "Librerías Gandhi",
            date = LocalDate.now(),
            description = "10% de descuento en libros de texto universitarios",
            category = CategoriaBeneficios.EDUCACION
        )
    )
}