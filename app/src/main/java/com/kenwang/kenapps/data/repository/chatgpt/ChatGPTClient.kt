package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class ChatGPTClient(
    private val chatGPTService: ChatGPTService
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getResult(keyword: String): JsonObject? {
        val response = checkAPIResponse { chatGPTService.getResult(ChatGPTRequest(prompt = keyword)) }
        return response.body()
    }
}
