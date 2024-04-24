package com.wenubey.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RickAndMortyTypeConverter::class)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract val dao: RickAndMortyDao
}