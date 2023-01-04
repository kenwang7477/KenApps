package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject
import com.kenwang.kenapps.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatGPTService {

    // https://beta.openai.com/playground?lang=curl
    @Headers(
        value = [
            "Content-Type: application/json",
            "Authorization: Bearer ${BuildConfig.ChatGPT_API_KEY}"
        ]
    )
    @POST("v1/completions")
    suspend fun getResult(@Body chatGPTRequest: ChatGPTRequest): Response<JsonObject>
}
