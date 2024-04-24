package com.wenubey.data.repository


import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.wenubey.data.KtorClient
import com.wenubey.data.local.CharacterEntity
import com.wenubey.data.local.toDomainCharacter
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val ktorClient: KtorClient,
    private val pager: Pager<Int, CharacterEntity>,
) : CharacterRepository {

    override fun getCharacterPage(): Flow<PagingData<Character>> =
        pager.flow.map { pagingData ->
            pagingData.map { it.toDomainCharacter() }
        }


    override suspend fun getCharacter(id: Int): Result<Character> {
        return ktorClient.getCharacter(id)
    }

}