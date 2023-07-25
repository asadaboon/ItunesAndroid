package com.example.demoapp2.domain.data


import com.google.gson.annotations.SerializedName

data class MusicResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val musicListResponse: ArrayList<MusicListResponse>
)