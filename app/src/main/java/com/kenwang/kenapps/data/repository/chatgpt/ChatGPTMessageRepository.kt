package com.kenwang.kenapps.data.repository.chatgpt

import com.kenwang.kenapps.data.model.ChatMessage

class ChatGPTMessageRepository(
    private val chatGPTMessageLocalDataSource: ChatGPTMessageLocalDataSource
) {

    fun getMessageList() = chatGPTMessageLocalDataSource.getMessageList()

    fun addMessage(chatMessage: ChatMessage): List<ChatMessage> {
        return chatGPTMessageLocalDataSource.addMessage(chatMessage)
    }
}
