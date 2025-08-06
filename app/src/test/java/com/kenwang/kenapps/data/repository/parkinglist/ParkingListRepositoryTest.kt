package com.kenwang.kenapps.data.repository.parkinglist

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.ParkingSpace
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
class ParkingListRepositoryTest {

    @MockK
    private lateinit var parkingListServerDataSource: ParkingListServerDataSource
    @MockK
    private lateinit var parkingListLocalDataSource: ParkingListLocalDataSource

    private lateinit var parkingListRepository: ParkingListRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        parkingListRepository = spyk(
            ParkingListRepository(parkingListServerDataSource, parkingListLocalDataSource)
        )
    }

    @Test
    fun `Test getParkingList with parkingListLocalDataSource parkingList is empty then parkingListLocalDataSource set result`() = runTest {
        val parkingList = listOf(ParkingSpace())
        every { parkingListLocalDataSource.parkingList } returns emptyList() andThen parkingList
        coEvery { parkingListServerDataSource.getParkingList() } returns parkingList

        parkingListRepository.getParkingList()

        Truth
            .assertThat(parkingListLocalDataSource.parkingList)
            .isEqualTo(parkingList)
        coVerify { parkingListServerDataSource.getParkingList() }
        verify { parkingListLocalDataSource.parkingList = parkingList }
    }

    @Test
    fun `Test getParkingList with parkingListLocalDataSource parkingList is not empty then parkingListLocalDataSource get result`() = runTest {
        val parkingList = listOf(ParkingSpace())
        every { parkingListLocalDataSource.parkingList } returns parkingList

        parkingListRepository.getParkingList()

        Truth
            .assertThat(parkingListLocalDataSource.parkingList)
            .isEqualTo(parkingList)
        coVerify(exactly = 0) { parkingListServerDataSource.getParkingList() }
    }
}
