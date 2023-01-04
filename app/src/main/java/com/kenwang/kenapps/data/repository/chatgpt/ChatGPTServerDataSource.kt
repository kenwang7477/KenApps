package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject

class ChatGPTServerDataSource(
    private val chatGPTClient: ChatGPTClient
) {

    suspend fun getResult(keyword: String): JsonObject? {
        return chatGPTClient.getResult(keyword)
    }
}
