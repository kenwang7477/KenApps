package com.kenwang.kenapps.data.repository.systempreference

import kotlinx.coroutines.flow.Flow

class SystemPreferenceRepository(
    private val systemPreferenceLocalDataStore: SystemPreferenceLocalDataStore
) {

    val darkMode: Flow<Boolean> = systemPreferenceLocalDataStore.darkMode

    suspend fun setDarkMode(darkMode: Boolean) {
        systemPreferenceLocalDataStore.setDarkMode(darkMode)
    }
}
