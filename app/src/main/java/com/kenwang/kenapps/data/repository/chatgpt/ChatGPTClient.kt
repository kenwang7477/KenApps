package com.kenwang.kenapps.data.repository.chatgpt

import com.google.gson.JsonObject
import java.net.SocketTimeoutException

class ChatGPTClient(
    private val chatGPTService: ChatGPTService
) {

    suspend fun getResult(keyword: String): JsonObject? {
        return try {
            chatGPTService
                .getResult(ChatGPTRequest(prompt = keyword))
                .body()
        } catch (e: SocketTimeoutException) {
            //TODO: 用自訂 error 包起來
            null
        } catch (e: Exception) {
            null
        }
    }
}
