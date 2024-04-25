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
import com.wenubey.data.local.RickAndMortyDao
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.remote.RickAndMortyRemoteMediator
import com.wenubey.data.remote.SearchQueryProviderImpl
import com.wenubey.data.repository.CharacterRepositoryImpl
import com.wenubey.data.repository.EpisodeRepositoryImpl
import com.wenubey.data.repository.UserPreferencesRepositoryImpl
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.domain.repository.EpisodeRepository
import com.wenubey.domain.repository.SearchQueryProvider
import com.wenubey.domain.repository.UserPreferencesRepository
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
    fun provideRickAndMortyPager(
        ktorClient: KtorClient,
        dao: RickAndMortyDao,
        searchQueryProvider: SearchQueryProvider
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = RickAndMortyRemoteMediator(
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
        searchQueryProvider: SearchQueryProvider
    ): CharacterRepository =
        CharacterRepositoryImpl(
            ktorClient = ktorClient,
            pager = pager,
            searchQueryProvider = searchQueryProvider
        )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideEpisodeRepository(ktorClient: KtorClient): EpisodeRepository =
        EpisodeRepositoryImpl(ktorClient)

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
    fun provideRickAndMortyDao(db: RickAndMortyDatabase): RickAndMortyDao = db.dao

    @Provides
    @Singleton
    fun provideSearchQueryProvider(): SearchQueryProvider = SearchQueryProviderImpl()
}