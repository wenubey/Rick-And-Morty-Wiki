package com.wenubey.domain.repository

import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage

interface CharacterRepository {

    suspend fun getCharacterPage(page: Int): Result<CharacterPage>

    suspend fun getCharacter(id: Int): Result<Character>
}