package com.wenubey.data.remote

import com.wenubey.domain.repository.SearchQueryProvider

class SearchQueryProviderImpl: SearchQueryProvider {

    private var searchQuery: String = ""

    override fun getSearchQuery(): String {
        return searchQuery
    }

    override fun setSearchQuery(query: String) {
        searchQuery = query
    }
}