package com.wenubey.rickandmortywiki.ui.di

import android.content.Context
import com.wenubey.data.ExoPlayerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayerProvider(@ApplicationContext context: Context): ExoPlayerProvider =
        ExoPlayerProvider(context)
}