package com.kenwang.kenapps.ui.setting

import com.google.common.truth.Truth
import com.kenwang.kenapps.domain.usecase.darkmode.GetDarkModeUseCase
import com.kenwang.kenapps.domain.usecase.darkmode.SetDarkModeUseCase
import com.kenwang.kenapps.mockProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    @MockK
    private lateinit var getDarkModeUseCase: GetDarkModeUseCase

    @MockK
    private lateinit var setDarkModeUseCase: SetDarkModeUseCase

    private lateinit var viewModel: SettingViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        val darkMode = flow { emit(true) }
        coEvery { getDarkModeUseCase.invoke() } returns darkMode
        viewModel = spyk(
            SettingViewModel(
                getDarkModeUseCase = getDarkModeUseCase.mockProvider(),
                setDarkModeUseCase = setDarkModeUseCase.mockProvider()
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Test setDarkMode then setDarkModeUseCase invoke`() = runTest {
        val darkMode = flow { emit(true) }
        coEvery { setDarkModeUseCase.invoke(any()) } returns darkMode
        val darkModeState = mutableListOf<Boolean>()
        val job = launch {
            viewModel.darkModeState.toList(darkModeState)
        }

        viewModel.setDarkMode(darkMode = true)
        runCurrent()

        coVerify(exactly = 1) {
            setDarkModeUseCase.invoke(darkMode = true)
        }
        Truth.assertThat(darkModeState[0]).isEqualTo(true)
        job.cancel()
    }
}
