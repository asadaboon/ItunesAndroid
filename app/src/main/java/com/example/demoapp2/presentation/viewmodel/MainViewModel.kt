package com.example.demoapp2.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp2.common.Results
import com.example.demoapp2.domain.data.MusicListResponse
import com.example.demoapp2.domain.data.MusicResponse
import com.example.demoapp2.domain.usecase.GetMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMusicUseCase: GetMusicUseCase,
) : ViewModel() {
    val searchMusicLiveData = MutableLiveData<Results<MusicResponse>>()
    val pairLiveData = MutableLiveData<Pair<ArrayList<MusicListResponse>, Boolean>>()
    private var updateMusicItem = ArrayList<MusicListResponse>()

    fun searchMusic(keyWord: String, limit: Int = 20) {
        getMusicUseCase(keyWord, limit).onStart {
            Log.e("BOONTHAM", "searchMusic: onStart")
        }.onEach { result ->
            when (result) {
                is Results.Loading -> {
                    searchMusicLiveData.value = result
                }

                is Results.Success -> {
                    searchMusicLiveData.value = result
                    result.data?.musicListResponse?.let {
                        updateMusicItem = it
                    }
                }

                is Results.Error -> {
                    searchMusicLiveData.value = result
                }
            }
        }.onCompletion {
            Log.e("BOONTHAM", "getList: onCompletion")
        }.launchIn(viewModelScope)
    }

    fun updateMusic(isPlay: Boolean, position: Int) {
        updateMusicItem[position].isPlaying = isPlay
        pairLiveData.value = Pair(updateMusicItem, isPlay)
    }
}