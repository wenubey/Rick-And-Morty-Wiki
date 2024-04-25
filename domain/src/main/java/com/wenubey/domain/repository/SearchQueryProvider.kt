package com.wenubey.domain.repository

interface SearchQueryProvider {
    fun getSearchQuery(): String

    fun setSearchQuery(query: String)
}