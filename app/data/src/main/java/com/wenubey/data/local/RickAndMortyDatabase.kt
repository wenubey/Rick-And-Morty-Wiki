package com.wenubey.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.domain.model.Location
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.RickAndMortyTypeConverter

@Database(
    entities = [Character::class, Location::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RickAndMortyTypeConverter::class)
abstract class RickAndMortyDatabase: RoomDatabase() {
    abstract val characterDao: CharacterDao
    abstract val locationDao: LocationDao
}