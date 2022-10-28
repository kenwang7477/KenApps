package com.kenwang.kenapps.ui.cctvtool.cctvlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.domain.usecase.cctvlist.GetCctvListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class CctvListViewModel @Inject constructor(
    private val getCctvListUseCase: Provider<GetCctvListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<CctvListViewState>(CctvListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        getCctvList()
    }

    fun getCctvList(keyword: String = "") {
        viewModelScope.launch {
            _viewState.emit(CctvListViewState.Loading)

            getCctvListUseCase.get()
                .apply {
                    setKeyword(keyword)
                }
                .invoke()
                .collect { result ->
                    when (result) {
                        is GetCctvListUseCase.Result.Empty -> {
                            _viewState.emit(CctvListViewState.Empty)
                        }

                        is GetCctvListUseCase.Result.Success -> {
                            _viewState.emit(
                                CctvListViewState.Success(result.cctvList.toImmutableList())
                            )
                        }
                    }
                }
        }
    }

    sealed class CctvListViewState {
        object Loading : CctvListViewState()
        object Empty : CctvListViewState()
        data class Success(val cctvList: ImmutableList<CctvMonitor>) : CctvListViewState()
    }
}
