package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject

class ChatGPTRepository(
    private val chatGPTServerDataSource: ChatGPTServerDataSource
) {

    suspend fun getResult(keyword: String): JsonObject? {
        return chatGPTServerDataSource.getResult(keyword)
    }
}
