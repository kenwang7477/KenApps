package com.kenwang.kenapps.domain.usecase.chatgpt

import com.kenwang.kenapps.data.repository.chatgpt.ChatGPTRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatGPTResultUseCase @Inject constructor(
    private val chatGPTRepository: ChatGPTRepository
){

    operator fun invoke(keyword: String) = flow {
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

        emit(Result.Success(result))
    }

    sealed class Result {
        data class Success(val result: String): Result()
    }
}
