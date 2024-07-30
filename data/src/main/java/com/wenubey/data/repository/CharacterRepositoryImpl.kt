package com.wenubey.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.wenubey.domain.RickAndMortyApi
import com.wenubey.domain.model.Character
import com.wenubey.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val pager: Pager<Int, Character>,
    private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    override fun getCharacterPage(): Flow<PagingData<Character>> = pager.flow

    override suspend fun getCharacter(id: Int): Result<Character> = withContext(ioDispatcher) {
        rickAndMortyApi.getCharacter(id)
    }

}