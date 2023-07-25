package com.example.demoapp2.common

sealed class Results<out T>(
    val status: ApiStatus,
    val data: T? = null,
    val message: String? = null
) {
    data class Loading<out R>(val _data: R?, val isLoading: Boolean) : Results<R>(
        status = ApiStatus.LOADING,
        data = _data,
        message = null
    )

    data class Success<out R>(val _data: R?) : Results<R>(
        status = ApiStatus.SUCCESS,
        data = _data,
        message = null
    )

    data class Error(val exception: String) : Results<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )
}

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}