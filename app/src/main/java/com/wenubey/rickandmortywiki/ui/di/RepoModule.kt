package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wenubey.data.KtorClient
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.LocationEntity
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao
import com.wenubey.data.remote.CharactersRemoteMediator
import com.wenubey.data.remote.LocationsRemoteMediator
import com.wenubey.data.repository.CharacterRepositoryImpl
import com.wenubey.data.repository.EpisodeRepositoryImpl
import com.wenubey.data.repository.LocationRepositoryImpl
import com.wenubey.data.repository.SettingsRepositoryImpl
import com.wenubey.data.repository.VideoPlayerRepositoryImpl
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
        ktorClient: KtorClient,
        dao: CharacterDao,
        searchQueryProvider: SearchQueryProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharactersRemoteMediator(
                ktorClient = ktorClient,
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
    fun provideLocationPager(
        ktorClient: KtorClient,
        dao: LocationDao,
        searchQueryProvider: SearchQueryProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): Pager<Int, LocationEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationsRemoteMediator(
                ktorClient = ktorClient,
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
        ktorClient: KtorClient,
        pager: Pager<Int, CharacterEntity>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CharacterRepository =
        CharacterRepositoryImpl(
            ktorClient = ktorClient,
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
    fun provideEpisodeRepository(ktorClient: KtorClient): EpisodeRepository =
        EpisodeRepositoryImpl(ktorClient)

    @Provides
    @Singleton
    fun provideLocationRepository(
        ktorClient: KtorClient,
        pager: Pager<Int, LocationEntity>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): LocationRepository = LocationRepositoryImpl(ktorClient, pager, ioDispatcher)

    @Provides
    @Singleton
    fun provideVideoPlayerRepository(
        @ApplicationContext context: Context,
    ): VideoPlayerRepository = VideoPlayerRepositoryImpl(context)
}