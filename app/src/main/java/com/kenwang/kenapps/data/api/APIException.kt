package com.kenwang.kenapps.data.api

sealed class APIException(
    val errorMessage: String
) : Exception() {
    object NetworkError : APIException("Network Error")
    object APITimeoutError : APIException("API Timeout Error")
    object UnknownError : APIException("Unknown Error")
}
