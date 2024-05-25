package com.wenubey.domain.repository

import com.wenubey.domain.model.DataTypeKey

interface SearchQueryProvider {
    fun getSearchQuery(key: DataTypeKey): String
    fun setSearchQuery(key: DataTypeKey, newQuery: String)
}