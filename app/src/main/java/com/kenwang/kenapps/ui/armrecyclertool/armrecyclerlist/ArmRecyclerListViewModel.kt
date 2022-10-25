package com.kenwang.kenapps.ui.armrecyclertool.armrecyclerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArmRecyclerListViewModel @Inject constructor(
    private val armRecyclerRepository: ArmRecyclerRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<ArmRecyclerListViewState>(ArmRecyclerListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        getArmRecyclerList()
    }

    private fun getArmRecyclerList() {
        viewModelScope.launch {
            _viewState.emit(
                ArmRecyclerListViewState.Success(
                    armRecyclerRepository.getArmRecyclerList()
                )
            )
        }
    }

    sealed class ArmRecyclerListViewState {
        object Loading : ArmRecyclerListViewState()
        data class Success(val armRecyclers: List<ArmRecycler>) : ArmRecyclerListViewState()
    }
}
