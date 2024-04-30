package com.wenubey.domain.model

data class Location(
    val id: Int,
    val name: String,
    val dimension: String,
    val residents: List<Int>,
    val url: String,
    val created: String,
    var locationResidents: List<Character>? = null,
) {

    companion object {
        fun default(): Location {
            return Location(
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