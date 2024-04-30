package com.wenubey.domain.model

data class Character(
    val created: String,
    val episodeIds: List<Int>,
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
            episodeIds = listOf(1,2,3,4,5),
            gender = CharacterGender.Male,
            id = 0,
            imageUrl = "",
            location = Location.default(),
            name = "Rick Sanchez",
            origin = Location.default(),
            species = "Human",
            status = "Unknown",
            type = ""
        )
    }


}