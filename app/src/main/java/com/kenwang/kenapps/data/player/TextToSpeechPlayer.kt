package com.kenwang.kenapps.data.player

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.TextToSpeech.QUEUE_FLUSH
import java.util.Locale

class TextToSpeechPlayer(
    private val context: Context
) {

    private var textToSpeech: TextToSpeech? = null
    private var speechRate = 1.0f

    private val onInitListener = OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.TAIWAN
            textToSpeech?.setSpeechRate(speechRate)
        }
    }

    init {
        resetTextToSpeech()
    }

    fun play(text: String) {
        textToSpeech?.speak(text, QUEUE_FLUSH, null, "")
    }

    fun setSpeechRate(speechRate: Float) {
        this.speechRate = speechRate
        textToSpeech?.setSpeechRate(speechRate)
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.shutdown()
    }

    fun resetTextToSpeech() {
        speechRate = 1.0f
        textToSpeech = TextToSpeech(context, onInitListener)
    }
}
