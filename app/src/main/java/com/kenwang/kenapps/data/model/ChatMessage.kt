package com.kenwang.kenapps.data.model

data class ChatMessage(
    val chatMessageFrom: ChatMessageFrom,
    val message: String,
    val time: Long
)

enum class ChatMessageFrom {
    ChatGPT, Me
}