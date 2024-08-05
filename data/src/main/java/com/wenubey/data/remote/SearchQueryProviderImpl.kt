package com.wenubey.data.remote

import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider
import timber.log.Timber

class SearchQueryProviderImpl : SearchQueryProvider {

    private val searchQueries = mutableMapOf<DataTypeKey, String>()
    override fun getSearchQuery(key: DataTypeKey): String {
        Timber.d("getSearchQuery: ${searchQueries[key]}")
        return searchQueries[key] ?: ""
    }

    override fun setSearchQuery(key: DataTypeKey, newQuery: String) {
        Timber.d("setSearchQuery: $newQuery")
        searchQueries[key] = newQuery
    }
}
