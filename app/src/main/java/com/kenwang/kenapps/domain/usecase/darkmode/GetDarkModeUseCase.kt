package com.kenwang.kenapps.domain.usecase.darkmode

import com.kenwang.kenapps.data.repository.systempreference.SystemPreferenceRepository
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val systemPreferenceRepository: SystemPreferenceRepository
) {

    operator fun invoke() = systemPreferenceRepository.darkMode
}
