package com.kenwang.kenapps.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenwang.kenapps.domain.usecase.darkmode.GetDarkModeUseCase
import com.kenwang.kenapps.domain.usecase.darkmode.SetDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDarkModeUseCase: Provider<GetDarkModeUseCase>,
    private val setDarkModeUseCase: Provider<SetDarkModeUseCase>
): ViewModel() {

    private val _darkModeState = MutableStateFlow(false)
    val darkModeState = _darkModeState.asStateFlow()

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            _darkModeState.emit(setDarkModeUseCase.get().invoke(darkMode).first())
        }
    }

    fun loadDarkMode() {
        viewModelScope.launch {
            _darkModeState.emit(getDarkModeUseCase.get().invoke().first())
        }
    }
}
