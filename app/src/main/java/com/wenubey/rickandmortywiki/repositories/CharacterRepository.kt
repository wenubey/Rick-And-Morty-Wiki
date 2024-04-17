package com.wenubey.rickandmortywiki.repositories

import com.wenubey.network.models.ApiOperation
import com.wenubey.network.models.domain.Character
import com.wenubey.network.models.domain.CharacterPage

interface CharacterRepository {

    suspend fun getCharacterPage(page: Int): ApiOperation<CharacterPage>

    suspend fun getCharacter(id: Int): ApiOperation<Character>
}