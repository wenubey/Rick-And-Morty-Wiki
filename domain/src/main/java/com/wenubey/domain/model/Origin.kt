package com.wenubey.domain.model

data class Origin(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String,
    val originResidents: List<Character>? = null
) {

    companion object {
        fun default(): Origin {
            return Origin(
                id = 1,
                name = "Earth",
                dimension = "Earth Dimension",
                residents = listOf(),
                url = "",
                created = ""
            )
        }
    }
}
