package com.wenubey.rickandmortywiki.ui.di

import com.wenubey.data.remote.SearchQueryProviderImpl
import com.wenubey.domain.repository.SearchQueryProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SearchModule {

    @Provides
    @Singleton
    fun provideSearchQueryProvider(): SearchQueryProvider = SearchQueryProviderImpl()
}