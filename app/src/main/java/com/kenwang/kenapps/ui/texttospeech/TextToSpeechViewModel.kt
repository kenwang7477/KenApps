package com.kenwang.kenapps.ui.texttospeech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.player.TextToSpeechPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class TextToSpeechViewModel @Inject constructor(
    private val textToSpeechPlayer: TextToSpeechPlayer
): ViewModel() {

    private val speechRateStateFlow = MutableStateFlow(1.0f)

    init {
        viewModelScope.launch {
            speechRateStateFlow.sample(500).collect {
                textToSpeechPlayer.setSpeechRate(it)
            }
        }
    }

    fun play(
        text: String
    ) {
        textToSpeechPlayer.play(text)
    }

    fun setSpeechRate(speechRate: Float) {
        viewModelScope.launch {
            speechRateStateFlow.emit(speechRate)
        }
    }

    fun stop() {
        textToSpeechPlayer.stop()
    }

    fun shutdown() {
        textToSpeechPlayer.shutdown()
    }

    fun reset() {
        textToSpeechPlayer.resetTextToSpeech()
    }
}
