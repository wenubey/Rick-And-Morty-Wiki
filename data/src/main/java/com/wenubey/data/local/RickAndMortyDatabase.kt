package com.wenubey.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao

@Database(
    entities = [CharacterEntity::class, LocationEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RickAndMortyTypeConverter::class)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val locationDao: LocationDao
}