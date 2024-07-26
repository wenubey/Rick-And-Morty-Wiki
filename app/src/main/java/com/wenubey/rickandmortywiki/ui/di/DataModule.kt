package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import androidx.room.Room
import com.wenubey.data.RickAndMortyApi
import com.wenubey.data.RickAndMortyApiClient
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideKtorClient(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): RickAndMortyApi = RickAndMortyApiClient(ioDispatcher = ioDispatcher)

    @Provides
    @Singleton
    fun provideRickAndMortyDatabase(@ApplicationContext context: Context): RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            "characters.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(db: RickAndMortyDatabase): CharacterDao = db.characterDao

    @Provides
    @Singleton
    fun provideLocationDao(db: RickAndMortyDatabase): LocationDao = db.locationDao

}