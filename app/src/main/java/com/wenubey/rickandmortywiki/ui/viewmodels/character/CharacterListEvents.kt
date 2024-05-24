package com.wenubey.rickandmortywiki.ui.viewmodels.character

interface CharacterListEvents {
    fun setSearchQuery(query: String)
    fun onActiveChange(active: Boolean)
    fun onSearch(query: String)
    fun setLastItemIndex(index: Int)
    fun removeAllQuery()
}