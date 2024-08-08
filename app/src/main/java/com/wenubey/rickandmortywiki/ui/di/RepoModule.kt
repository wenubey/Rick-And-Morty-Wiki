package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.data.local.dao.RemoteKeyDao
import com.wenubey.data.remote.CharactersRemoteMediator
import com.wenubey.data.remote.LocationsRemoteMediator
import com.wenubey.data.repository.CharacterRepositoryImpl
import com.wenubey.data.repository.EpisodeRepositoryImpl
import com.wenubey.data.repository.LocationRepositoryImpl
import com.wenubey.data.repository.SettingsRepositoryImpl
import com.wenubey.data.repository.VideoPlayerRepositoryImpl
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Character
import com.wenubey.domain.model.Location
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.EpisodeRepository
import com.wenubey.domain.repository.LocationRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.SettingsRepository
import com.wenubey.domain.repository.VideoPlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideCharacterPager(
        rickAndMortyApi: RickAndMortyApi,
        dao: CharacterDao,
        searchQueryProvider: SearchQueryProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        remoteKeyDao: RemoteKeyDao,
    ): Pager<Int, Character> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false, prefetchDistance = 1),
            remoteMediator = CharactersRemoteMediator(
                rickAndMortyApi = rickAndMortyApi,
                dao = dao,
                searchQueryProvider = searchQueryProvider,
                ioDispatcher = ioDispatcher,
                remoteKeyDao = remoteKeyDao
            ),
            initialKey = 1,
            pagingSourceFactory = {
                dao.pagingSource()
            },
        )
    }

    @Provides
    @Singleton
    fun provideLocationPager(
        rickAndMortyApi: RickAndMortyApi,
        dao: LocationDao,
        searchQueryProvider: SearchQueryProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): Pager<Int, Location> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationsRemoteMediator(
                rickAndMortyApi = rickAndMortyApi,
                dao = dao,
                searchQueryProvider = searchQueryProvider,
                ioDispatcher = ioDispatcher
            ),
            pagingSourceFactory = {
                dao.pagingSource()
            }
        )
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        rickAndMortyApi: RickAndMortyApi,
        pager: Pager<Int, Character>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CharacterRepository =
        CharacterRepositoryImpl(
            rickAndMortyApi = rickAndMortyApi,
            pager = pager,
            ioDispatcher = ioDispatcher
        )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): SettingsRepository =
        SettingsRepositoryImpl(dataStore, ioDispatcher)

    @Provides
    @Singleton
    fun provideEpisodeRepository(rickAndMortyApi: RickAndMortyApi): EpisodeRepository =
        EpisodeRepositoryImpl(rickAndMortyApi)

    @Provides
    @Singleton
    fun provideLocationRepository(
        rickAndMortyApi: RickAndMortyApi,
        pager: Pager<Int, Location>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): LocationRepository = LocationRepositoryImpl(rickAndMortyApi, pager, ioDispatcher)

    @Provides
    @Singleton
    fun provideVideoPlayerRepository(
        @ApplicationContext context: Context,
    ): VideoPlayerRepository = VideoPlayerRepositoryImpl(context)
}