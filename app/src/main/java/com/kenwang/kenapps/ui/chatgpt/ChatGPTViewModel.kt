package com.kenwang.kenapps.ui.chatgpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ChatMessage
import com.kenwang.kenapps.domain.usecase.chatgpt.GetChatGPTResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class ChatGPTViewModel @Inject constructor(
    private val getChatGPTResultUseCase: Provider<GetChatGPTResultUseCase>
) : ViewModel() {

    private var _viewState = MutableStateFlow<ChatGPTViewState>(ChatGPTViewState.Empty)
    val viewState = _viewState.asStateFlow()

    private var _actionState = MutableSharedFlow<ChatGPTActionState>()
    val actionState = _actionState.asSharedFlow()


    fun getResult(keyword: String) {
        getChatGPTResultUseCase.get().invoke(keyword)
            .onEach {
                when(it) {
                    is GetChatGPTResultUseCase.Result.Success -> {
                        _viewState.emit(ChatGPTViewState.ShowResult(it.messageList.toImmutableList()))
                    }
                    is GetChatGPTResultUseCase.Result.Error -> {
                        _actionState.emit(
                            ChatGPTActionState.ShowErrorToast(it.exception.errorMessage)
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    sealed class ChatGPTViewState {
        object Empty : ChatGPTViewState()
        object Loading : ChatGPTViewState()
        data class ShowResult(val messageList: ImmutableList<ChatMessage>) : ChatGPTViewState()
    }

    sealed class ChatGPTActionState {
        data class ShowErrorToast(val errorMessage: String) : ChatGPTActionState()
    }
}
