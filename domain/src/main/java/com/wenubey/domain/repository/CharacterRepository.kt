package com.wenubey.domain.repository

import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacterPage(name: String?): Flow<PagingData<Character>>

    suspend fun getCharacter(id: Int): Result<Character>

    suspend fun getLocationResidents(residentUrls: List<Int>): Result<List<Character>>
}