package com.wenubey.domain.model

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RickAndMortyTypeConverter {

    private inline fun <reified T : Any> fromJson(json: String?): T? {
        return json?.let {
            try {
                Json.decodeFromString<T>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    private inline fun <reified T : Any> toJson(data: T?): String? {
        return data?.let {
            Json.encodeToString(it)
        }
    }

    @TypeConverter
    fun toLocationEntityJson(location: Location): String? {
        return toJson(location)
    }

    @TypeConverter
    fun fromJsonLocationEntity(json: String?): Location? {
        return fromJson(json)
    }

    @TypeConverter
    fun toCharacterGender(value: String?): CharacterGender? {
        return when (value) {
            "Male" -> CharacterGender.Male
            "Female" -> CharacterGender.Female
            "No gender" -> CharacterGender.Genderless
            "Not specified" -> CharacterGender.Unknown
            else -> null
        }
    }

    @TypeConverter
    fun fromCharacterGender(gender: CharacterGender?): String? {
        return gender?.displayName
    }

    @TypeConverter
    fun toEpisodeListJson(episodes: List<Episode>): String? {
        return toJson(episodes)
    }

    @TypeConverter
    fun fromJsonEpisodeList(json: String?): List<Episode>? {
        return fromJson(json)
    }

    @TypeConverter
    fun toCharacterListJson(characters: List<Character>): String? {
        return toJson(characters)
    }

    @TypeConverter
    fun fromJsonCharacterList(json: String?): List<Character>? {
        return fromJson(json)
    }
}