package com.wenubey.data.remote

import com.wenubey.domain.repository.SearchQueryProvider

class SearchQueryProviderImpl : SearchQueryProvider {

    private var _characterQuery = ""
    private var _locationQuery = ""

    override fun getCharacterSearchQuery(): String {
        return _characterQuery
    }

    override fun setCharacterSearchQuery(query: String) {
        _characterQuery = query
    }

    override fun getLocationSearchQuery(): String {
        return _locationQuery
    }

    override fun setLocationSearchQuery(query: String) {
        _locationQuery = query
    }
}