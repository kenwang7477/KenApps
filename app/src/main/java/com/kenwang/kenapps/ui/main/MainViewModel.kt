package com.kenwang.kenapps.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.domain.usecase.main.GetMainListUseCase
import com.kenwang.kenapps.domain.usecase.main.MainListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMainListUseCase: Provider<GetMainListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainViewState>(MainViewState.Empty)
    val viewState = _viewState.asStateFlow()

    init {
        getMainList()
    }

    private fun getMainList() {
        viewModelScope.launch {
            getMainListUseCase.get().invoke().collect { result ->
                _viewState.emit(MainViewState.Success(result.list.toList()))
            }
        }
    }

    sealed class MainViewState {
        object Empty : MainViewState()
        data class Success(val items: List<MainListItem>) : MainViewState()
    }
}
