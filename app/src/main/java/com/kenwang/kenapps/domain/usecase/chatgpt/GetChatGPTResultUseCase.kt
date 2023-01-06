package com.kenwang.kenapps.domain.usecase.chatgpt

import com.kenwang.kenapps.data.model.ChatMessage
import com.kenwang.kenapps.data.model.ChatMessageFrom
import com.kenwang.kenapps.data.repository.chatgpt.ChatGPTMessageRepository
import com.kenwang.kenapps.data.repository.chatgpt.ChatGPTRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatGPTResultUseCase @Inject constructor(
    private val chatGPTRepository: ChatGPTRepository,
    private val chatGPTMessageRepository: ChatGPTMessageRepository
){

    operator fun invoke(keyword: String) = flow {
        chatGPTMessageRepository.addMessage(
            ChatMessage(
                ChatMessageFrom.Me,
                keyword,
                System.currentTimeMillis()
            )
        )
        emit(Result.Success(chatGPTMessageRepository.getMessageList()))
        //TODO: 依 repository error 改變
        val noResult = "No Result!"
        val result = chatGPTRepository.getResult(keyword)?.let {
            if (it.has("choices")) {
                val rawText = it.getAsJsonArray("choices")
                    .get(0)
                    .asJsonObject
                    .get("text")
                    .toString()
                if (rawText.startsWith("\"") && rawText.endsWith("\"")) {
                    rawText.subSequence(1, rawText.length - 2).toString()
                } else {
                    rawText
                }.replace("\\n", "")
            } else {
                noResult
            }
        } ?: noResult

        chatGPTMessageRepository.addMessage(
            ChatMessage(
                ChatMessageFrom.ChatGPT,
                result,
                System.currentTimeMillis()
            )
        )

        emit(Result.Success(chatGPTMessageRepository.getMessageList()))
    }

    sealed class Result {
        data class Success(val messageList: List<ChatMessage>): Result()
    }
}
