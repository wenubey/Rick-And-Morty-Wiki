package com.wenubey.rickandmortywiki.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wenubey.network.KtorClient
import com.wenubey.rickandmortywiki.repositories.CharacterRepository
import com.wenubey.rickandmortywiki.repositories.CharacterRepositoryImpl
import com.wenubey.rickandmortywiki.repositories.EpisodeRepository
import com.wenubey.rickandmortywiki.repositories.EpisodeRepositoryImpl
import com.wenubey.rickandmortywiki.repositories.UserPreferencesRepository
import com.wenubey.rickandmortywiki.repositories.UserPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

private const val USER_PREFERENCE_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKtorClient(): KtorClient {
        return KtorClient()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    @Singleton
    fun provideCharacterRepository(ktorClient: KtorClient): CharacterRepository = CharacterRepositoryImpl(ktorClient)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository = UserPreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun provideEpisodeRepository(ktorClient: KtorClient): EpisodeRepository = EpisodeRepositoryImpl(ktorClient)
}