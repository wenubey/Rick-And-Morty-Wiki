package com.wenubey.data.remote

import com.wenubey.domain.model.DataTypeKey
import com.wenubey.domain.repository.SearchQueryProvider

class SearchQueryProviderImpl : SearchQueryProvider {

    private val searchQueries = mutableMapOf<DataTypeKey, String>()
    override fun getSearchQuery(key: DataTypeKey): String {
        return searchQueries[key] ?: ""
    }

    override fun setSearchQuery(key: DataTypeKey, newQuery: String) {
        searchQueries[key] = newQuery
    }
}
