package com.example.demoapp2.data.repository

import com.example.demoapp2.domain.data.MusicResponse

interface MainRepository {

    suspend fun searchMusic(term: String, limit: Int): MusicResponse
}