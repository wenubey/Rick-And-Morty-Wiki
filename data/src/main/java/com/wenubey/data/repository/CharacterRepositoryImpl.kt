package com.wenubey.data.repository


import com.wenubey.domain.model.Character
import com.wenubey.domain.model.CharacterPage
import com.wenubey.domain.repository.CharacterRepository
import com.wenubey.data.KtorClient
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient
): CharacterRepository {

    override suspend fun getCharacterPage(page: Int): Result<CharacterPage> {
       return ktorClient.getCharacterPage(page)
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return ktorClient.getCharacter(id)
    }

}