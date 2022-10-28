package com.kenwang.kenapps.ui.armrecyclertool.armrecyclerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.domain.usecase.armrecyclerlist.GetArmRecyclerListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class ArmRecyclerListViewModel @Inject constructor(
    private val getArmRecyclerListUseCase: Provider<GetArmRecyclerListUseCase>
) : ViewModel() {

    private val _viewState = MutableStateFlow<ArmRecyclerListViewState>(ArmRecyclerListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        getArmRecyclerList()
    }

    private fun getArmRecyclerList() {
        viewModelScope.launch {
            _viewState.emit(ArmRecyclerListViewState.Loading)

            getArmRecyclerListUseCase.get().invoke().collect { result ->
                when (result) {
                    is GetArmRecyclerListUseCase.Result.Empty -> {
                        _viewState.emit(ArmRecyclerListViewState.Empty)
                    }
                    is GetArmRecyclerListUseCase.Result.Success -> {
                        _viewState.emit(
                            ArmRecyclerListViewState.Success(result.list.toImmutableList())
                        )
                    }
                }
            }
        }
    }

    sealed class ArmRecyclerListViewState {
        object Loading : ArmRecyclerListViewState()
        object Empty : ArmRecyclerListViewState()
        data class Success(val armRecyclers: ImmutableList<ArmRecycler>) : ArmRecyclerListViewState()
    }
}
