package com.wenubey.domain.repository

import androidx.paging.PagingData
import com.wenubey.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    fun getCharacterPage(): Flow<PagingData<Character>>

    suspend fun getCharacter(id: Int): Result<Character>
}