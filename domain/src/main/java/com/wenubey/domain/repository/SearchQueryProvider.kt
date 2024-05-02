package com.wenubey.domain.repository

interface SearchQueryProvider {


    fun getCharacterSearchQuery(): String

    fun setCharacterSearchQuery(query: String)

    fun getLocationSearchQuery(): String

    fun setLocationSearchQuery(query: String)

}