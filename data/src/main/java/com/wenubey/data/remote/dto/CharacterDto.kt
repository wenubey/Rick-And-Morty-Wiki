package com.wenubey.data.remote.dto

import com.wenubey.data.local.CharacterEntity
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginResponse,
    val location: LocationResponse,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) {

    fun toCharacterEntity(
        locationDto: LocationDto,
        originDto: OriginDto,
    ): CharacterEntity {
        return CharacterEntity(
            created = created,
            episode = episode,
            gender = gender,
            id = id,
            image = image,
            locationEntity = location.toLocationDto(locationDto).toLocationEntity(),
            name = name,
            originEntity = origin.toOriginDto(originDto).toOriginEntity(),
            species = species,
            status = status,
            type = type,
            url = url
        )
    }

}

fun LocationResponse.toLocationDto(
    locationDto: LocationDto
): LocationDto {
    return LocationDto(
        id = locationDto.id,
        name = this.name,
        dimension = locationDto.dimension,
        residents = locationDto.residents,
        url = this.url,
        created = locationDto.created
    )
}

fun OriginResponse.toOriginDto(
    originDto: OriginDto
): OriginDto {
    return OriginDto(
        id = originDto.id,
        name = this.name,
        dimension = originDto.dimension,
        residents = originDto.residents,
        url = url.ifBlank { null },
        created = originDto.created
    )
}

