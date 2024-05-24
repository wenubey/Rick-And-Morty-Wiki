package com.wenubey.rickandmortywiki.ui.viewmodels

interface ListScreenEvents {
    fun setSearchQuery(query: String)
    fun onActiveChange(active: Boolean)
    fun onSearch(query: String)
    fun setLastItemIndex(index: Int)
    fun removeAllQuery()
}