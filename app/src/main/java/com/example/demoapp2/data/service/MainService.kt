package com.example.demoapp2.data.service

import com.example.demoapp2.domain.data.MusicResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {
    @GET("search")
    suspend fun searchMusic(
        @Query("term") term: String,
        @Query("limit") limit: Int
    ): MusicResponse
}