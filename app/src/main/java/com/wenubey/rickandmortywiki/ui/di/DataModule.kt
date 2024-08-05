package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import androidx.room.Room
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.data.local.RickAndMortyDatabase
import com.wenubey.data.local.dao.CharacterDao
import com.wenubey.data.local.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideKtorClient(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClient: HttpClient,
    ): RickAndMortyApi =
        com.wenubey.data.RickAndMortyApiClient(ioDispatcher = ioDispatcher, httpClient = httpClient)

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest { url(BASE_URL) }

            install(Logging) {
                logger = Logger.ANDROID
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }



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


    private const val BASE_URL = "https://rickandmortyapi.com/api/"

}