package com.wenubey.domain.model

data class Character(
    val created: String,
    val episodes: List<Episode>,
    val gender: CharacterGender,
    val id: Int,
    val imageUrl: String,
    val location: Location,
    val name: String,
    val origin: Location,
    val species: String,
    val status: String,
    val type: String,
) {

    companion object {
        fun default() = Character(
            created = "created",
            episodes = listOf(),
            gender = CharacterGender.Male,
            id = 0,
            imageUrl = "",
            location = Location.default(),
            name = "Abadango Cluster",
            origin = Location.default(),
            species = "Human",
            status = "Unknown",
            type = ""
        )
    }


}