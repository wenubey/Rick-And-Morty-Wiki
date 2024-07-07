package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.wenubey.data.KtorClient
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.LocationEntity
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.data.remote.CharactersRemoteMediator
import com.wenubey.data.remote.LocationsRemoteMediator
import com.wenubey.data.remote.SearchQueryProviderImpl
import com.wenubey.data.repository.CharacterRepositoryImpl
import com.wenubey.data.repository.EpisodeRepositoryImpl
import com.wenubey.data.repository.LocationRepositoryImpl
import com.wenubey.data.repository.SettingsRepositoryImpl
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.EpisodeRepository
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private const val USER_PREFERENCE_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKtorClient(): KtorClient = KtorClient()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore


    @Provides
    @Singleton
    fun provideCharacterPager(
        ktorClient: KtorClient,
        dao: CharacterDao,
        searchQueryProvider: SearchQueryProvider
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharactersRemoteMediator(
                ktorClient = ktorClient,
                dao = dao,
                searchQueryProvider = searchQueryProvider
            ),
            pagingSourceFactory = {
                dao.pagingSource()
            }
        )
    }

    @Provides
    @Singleton
    fun provideLocationPager(
        ktorClient: KtorClient,
        dao: LocationDao,
        searchQueryProvider: SearchQueryProvider
    ): Pager<Int, LocationEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationsRemoteMediator(
                ktorClient = ktorClient,
                dao = dao,
                searchQueryProvider = searchQueryProvider
            ),
            pagingSourceFactory = {
                dao.pagingSource()
            }
        )
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        ktorClient: KtorClient,
        pager: Pager<Int, CharacterEntity>,
    ): CharacterRepository =
        CharacterRepositoryImpl(
            ktorClient = ktorClient,
            pager = pager,
        )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): SettingsRepository =
        SettingsRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideEpisodeRepository(ktorClient: KtorClient): EpisodeRepository =
        EpisodeRepositoryImpl(ktorClient)

    @Provides
    @Singleton
    fun provideLocationRepository(
        ktorClient: KtorClient,
        pager: Pager<Int, LocationEntity>
    ): LocationRepository = LocationRepositoryImpl(ktorClient, pager)

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

    @Provides
    @Singleton
    fun provideSearchQueryProvider(): SearchQueryProvider = SearchQueryProviderImpl()
}