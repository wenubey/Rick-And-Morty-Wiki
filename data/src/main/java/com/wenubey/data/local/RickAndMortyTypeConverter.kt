package com.wenubey.data.local

import androidx.room.TypeConverter

class RickAndMortyTypeConverter {


    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        return data.split(",")
    }
}