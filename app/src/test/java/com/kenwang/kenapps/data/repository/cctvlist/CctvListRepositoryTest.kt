package com.kenwang.kenapps.data.repository.cctvlist

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.CctvMonitor
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CctvListRepositoryTest {

    @MockK
    private lateinit var cctvListServerDataSource: CctvListServerDataSource
    @MockK
    private lateinit var cctvListLocalDataSource: CctvListLocalDataSource

    private lateinit var cctvListRepository: CctvListRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        cctvListRepository = spyk(
            CctvListRepository(cctvListServerDataSource, cctvListLocalDataSource)
        )
    }

    @Test
    fun `Test getCctvList with cctvListLocalDataSource cctvList is empty then cctvListLocalDataSource set result`() = runTest {
        val cctvList = listOf(CctvMonitor())
        every { cctvListLocalDataSource.cctvList } returns emptyList() andThen cctvList
        coEvery { cctvListServerDataSource.getCctvList() } returns cctvList

        cctvListRepository.getCctvList()

        Truth
            .assertThat(cctvListLocalDataSource.cctvList)
            .isEqualTo(cctvList)
        coVerify { cctvListServerDataSource.getCctvList() }
        verify { cctvListLocalDataSource.cctvList = cctvList }
    }

    @Test
    fun `Test getCctvList with cctvListLocalDataSource cctvList is not empty then cctvListLocalDataSource get result`() = runTest {
        val cctvList = listOf(CctvMonitor())
        every { cctvListLocalDataSource.cctvList } returns cctvList
        coEvery { cctvListServerDataSource.getCctvList() } returns cctvList

        cctvListRepository.getCctvList()

        Truth
            .assertThat(cctvListLocalDataSource.cctvList)
            .isEqualTo(cctvList)
        coVerify(exactly = 0) { cctvListServerDataSource.getCctvList() }
    }
}
