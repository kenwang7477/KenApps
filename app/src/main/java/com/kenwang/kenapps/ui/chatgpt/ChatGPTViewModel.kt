package com.kenwang.kenapps.ui.chatgpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.domain.usecase.chatgpt.GetChatGPTResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class ChatGPTViewModel @Inject constructor(
    private val getChatGPTResultUseCase: Provider<GetChatGPTResultUseCase>
) : ViewModel() {

    private var _viewState = MutableStateFlow<ChatGPTViewState>(ChatGPTViewState.Empty)
    val viewState = _viewState.asStateFlow()

    fun getResult(keyword: String) {
        viewModelScope.launch {
            _viewState.emit(ChatGPTViewState.Loading)
            getChatGPTResultUseCase.get().invoke(keyword).collect {
                _viewState.emit(ChatGPTViewState.ShowResult(it.result))
            }
        }
    }

    sealed class ChatGPTViewState {
        object Empty : ChatGPTViewState()
        object Loading : ChatGPTViewState()
        data class ShowResult(val result: String) : ChatGPTViewState()
    }
}
