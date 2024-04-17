package com.wenubey.rickandmortywiki.repositories

import com.wenubey.network.KtorClient
import com.wenubey.network.models.ApiOperation
import com.wenubey.network.models.domain.Character
import com.wenubey.network.models.domain.CharacterPage
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient
): CharacterRepository {

    override suspend fun getCharacterPage(page: Int): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterPage(page)
    }

    override suspend fun getCharacter(id: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(id)
    }
}