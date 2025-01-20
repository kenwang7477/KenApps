package com.kenwang.kenapps.ui.setting

import app.cash.turbine.test
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

        viewModel.setDarkMode(darkMode = true)
        runCurrent()

        viewModel.darkModeState.test {
            Truth.assertThat(awaitItem()).isEqualTo(true)
        }
        coVerify(exactly = 1) {
            setDarkModeUseCase.invoke(darkMode = true)
        }
    }
}
