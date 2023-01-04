package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject

class ChatGPTClient(
    private val chatGPTService: ChatGPTService
) {

    suspend fun getResult(keyword: String): JsonObject? {
        return chatGPTService
            .getResult(ChatGPTRequest(prompt = keyword))
            .body()
    }
}
