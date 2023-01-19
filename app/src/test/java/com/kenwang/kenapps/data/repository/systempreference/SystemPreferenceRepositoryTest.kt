package com.kenwang.kenapps.data.repository.systempreference

import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SystemPreferenceRepositoryTest {

    @MockK
    private lateinit var systemPreferenceLocalDataStore: SystemPreferenceLocalDataStore

    private lateinit var systemPreferenceRepository: SystemPreferenceRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        systemPreferenceRepository = spyk(
            SystemPreferenceRepository(systemPreferenceLocalDataStore)
        )
    }

    @Test
    fun `Test setDarkMode then systemPreferenceRepository setDarkMode invoke`() = runTest {
        val darkMode = true

        systemPreferenceRepository.setDarkMode(darkMode)

        coVerify { systemPreferenceLocalDataStore.setDarkMode(darkMode) }
    }
}
