package com.kenwang.kenapps.data.repository.armrecycler

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.ArmRecycler
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
class ArmRecyclerRepositoryTest {

    @MockK
    private lateinit var armRecyclerServerDataSource: ArmRecyclerServerDataSource
    @MockK
    private lateinit var armRecyclerLocalDataSource: ArmRecyclerLocalDataSource

    private lateinit var armRecyclerRepository: ArmRecyclerRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        armRecyclerRepository = spyk(
            ArmRecyclerRepository(
                armRecyclerServerDataSource,
                armRecyclerLocalDataSource
            )
        )
    }

    @Test
    fun `Test getArmRecyclerList with armRecyclerLocalDataSource armRecyclerList is empty then armRecyclerLocalDataSource set result`() = runTest {
        val armRecyclerList = listOf(ArmRecycler())
        every { armRecyclerLocalDataSource.armRecyclerList } returns emptyList() andThen armRecyclerList
        coEvery { armRecyclerServerDataSource.getArmRecyclerList() } returns armRecyclerList

        armRecyclerRepository.getArmRecyclerList()

        Truth
            .assertThat(armRecyclerLocalDataSource.armRecyclerList)
            .isEqualTo(armRecyclerList)
        coVerify { armRecyclerServerDataSource.getArmRecyclerList() }
        verify { armRecyclerLocalDataSource.armRecyclerList = armRecyclerList }
    }

    @Test
    fun `Test getArmRecyclerList with armRecyclerLocalDataSource armRecyclerList is not empty then armRecyclerLocalDataSource get result`() = runTest {
        val armRecyclerList = listOf(ArmRecycler())
        every { armRecyclerLocalDataSource.armRecyclerList } returns armRecyclerList

        armRecyclerRepository.getArmRecyclerList()

        Truth
            .assertThat(armRecyclerLocalDataSource.armRecyclerList)
            .isEqualTo(armRecyclerList)
        coVerify(exactly = 0) { armRecyclerServerDataSource.getArmRecyclerList() }
    }
}
