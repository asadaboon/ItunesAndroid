package com.example.demoapp2.data.repository

import com.example.demoapp2.data.service.MainService
import com.example.demoapp2.domain.data.MusicResponse
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val service: MainService
) : MainRepository {

    override suspend fun searchMusic(term: String, limit: Int): MusicResponse {
        return service.searchMusic(term, limit)
    }
}