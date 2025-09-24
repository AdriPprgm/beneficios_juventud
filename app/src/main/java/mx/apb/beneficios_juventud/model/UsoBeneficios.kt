package mx.apb.beneficios_juventud.model

import java.time.LocalDate

data class UsoBeneficios(
    val id: String,
    val merchant: String,
    val date: LocalDate,
    val description: String,
    val category: CategoriaBeneficios
)
