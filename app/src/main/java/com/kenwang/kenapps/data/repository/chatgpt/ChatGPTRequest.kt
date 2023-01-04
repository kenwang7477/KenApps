package com.kenwang.kenapps.data.repository.chatgpt

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatGPTRequest(
    val model: String = "text-davinci-003",
    val prompt: String = "",
    val temperature: Float = 0.7f,
    val max_tokens: Int = 256,
    val top_p: Float = 1.0f,
    val frequency_penalty: Float = 0.0f,
    val presence_penalty: Float = 0.0f
) : Parcelable
