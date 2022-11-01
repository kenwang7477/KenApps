package com.kenwang.kenapps.domain.usecase.darkmode

import com.kenwang.kenapps.data.repository.systempreference.SystemPreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetDarkModeUseCase @Inject constructor(
    private val systemPreferenceRepository: SystemPreferenceRepository
) {

    suspend operator fun invoke(darkMode: Boolean): Flow<Boolean> {
        systemPreferenceRepository.setDarkMode(darkMode)
        return systemPreferenceRepository.darkMode
    }
}
