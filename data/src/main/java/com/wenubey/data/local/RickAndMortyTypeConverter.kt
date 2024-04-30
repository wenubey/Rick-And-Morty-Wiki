package com.wenubey.data.local

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
    fun toLocationEntityJson(locationEntity: LocationEntity): String? {
        return toJson(locationEntity)
    }

    @TypeConverter
    fun fromJsonLocationEntity(json: String?): LocationEntity? {
        return fromJson(json)
    }

    @TypeConverter
    fun toOriginEntityJson(originEntity: OriginEntity): String? {
        return toJson(originEntity)
    }

    @TypeConverter
    fun fromJsonOriginEntity(json: String?): OriginEntity? {
        return fromJson(json)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return data.split(",")
    }
}