package com.wenubey.domain.model

data class Character(
    val created: String,
    val episodeIds: List<Int>,
    val gender: CharacterGender,
    val id: Int,
    val imageUrl: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String
) {
    data class Location(
        val name: String,
        val url: String
    )

    data class Origin(
        val name: String,
        val url: String
    )

    companion object {
        fun default() = Character(
            created = "created",
            episodeIds = listOf(1,2,3,4,5),
            gender = CharacterGender.Male,
            id = 0,
            imageUrl = "",
            location = Location("Earth", ""),
            name = "Rick Sanchez",
            origin = Origin("Earth", ""),
            species = "Human",
            status = "Unknown",
            type = ""
        )
    }


}