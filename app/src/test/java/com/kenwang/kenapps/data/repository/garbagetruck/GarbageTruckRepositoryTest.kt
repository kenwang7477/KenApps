package com.kenwang.kenapps.data.repository.garbagetruck

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.GarbageTruck
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GarbageTruckRepositoryTest {

    @MockK
    private lateinit var garbageTruckServerDataSource: GarbageTruckServerDataSource
    @MockK
    private lateinit var garbageTruckLocalDataSource: GarbageTruckLocalDataSource

    private lateinit var garbageTruckRepository: GarbageTruckRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        garbageTruckRepository = spyk(
            GarbageTruckRepository(garbageTruckServerDataSource, garbageTruckLocalDataSource)
        )
    }

    @Test
    fun `Test getTrucks with garbageTruckLocalDataSource truckList is empty then garbageTruckLocalDataSource set result`() = runTest {
        val truckList = listOf(GarbageTruck())
        every { garbageTruckLocalDataSource.trucks } returns emptyList() andThen truckList
        coEvery { garbageTruckServerDataSource.getTrucks() } returns truckList

        garbageTruckRepository.getTrucks()

        Truth
            .assertThat(garbageTruckLocalDataSource.trucks)
            .isEqualTo(truckList)
        coVerify { garbageTruckServerDataSource.getTrucks() }
        verify { garbageTruckLocalDataSource.trucks = truckList }
    }

    @Test
    fun `Test getTrucks with garbageTruckLocalDataSource trucks is not empty then garbageTruckLocalDataSource get result`() = runTest {
        val truckList = listOf(GarbageTruck())
        every { garbageTruckLocalDataSource.trucks } returns truckList

        garbageTruckRepository.getTrucks()

        Truth
            .assertThat(garbageTruckLocalDataSource.trucks)
            .isEqualTo(truckList)
        coVerify(exactly = 0) { garbageTruckServerDataSource.getTrucks() }
    }

    @Test
    fun `Test getTrucks with forceUpdate then set garbageTruckLocalDataSource trucks to empty`() = runTest {
        val emptyTruckList = emptyList<GarbageTruck>()
        val serverTruckList = listOf(GarbageTruck())
        coEvery { garbageTruckServerDataSource.getTrucks() } returns serverTruckList
        every { garbageTruckLocalDataSource.trucks } returns emptyTruckList

        garbageTruckRepository.getTrucks(true)

        verifyOrder {
            garbageTruckLocalDataSource.trucks = emptyTruckList
            garbageTruckLocalDataSource.trucks = serverTruckList
        }
    }
}
