package com.example.demoapp2.domain.usecase

import com.example.demoapp2.common.Results
import com.example.demoapp2.data.repository.MainRepository
import com.example.demoapp2.domain.data.MusicResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMusicUseCase @Inject constructor(
    private val repository: MainRepository
) {
    operator fun invoke(keyWord: String, limit: Int): Flow<Results<MusicResponse>> = flow {
        try {
            emit(Results.Success(repository.searchMusic(keyWord, limit)))
        } catch (e: HttpException) {
            emit(Results.Error("$e"))
        } catch (e: IOException) {
            emit(Results.Error("$e"))
        }
    }
}