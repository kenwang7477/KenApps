package com.kenwang.kenapps.data.repository.chatgpt

import com.kenwang.kenapps.data.model.ChatMessage

class ChatGPTMessageLocalDataSource {

    private val messageList = mutableListOf<ChatMessage>()

    fun getMessageList() = messageList

    fun addMessage(chatMessage: ChatMessage): List<ChatMessage> {
        messageList.add(chatMessage)
        return messageList
    }
}
