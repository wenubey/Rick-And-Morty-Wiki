package com.wenubey.data.remote

import com.wenubey.domain.repository.SearchQueryProvider

class SearchQueryProviderImpl: SearchQueryProvider {

    private var _searchQuery = ""

    override fun getSearchQuery(): String {
        return _searchQuery
    }

    override fun setSearchQuery(query: String) {
        _searchQuery = query
    }
}