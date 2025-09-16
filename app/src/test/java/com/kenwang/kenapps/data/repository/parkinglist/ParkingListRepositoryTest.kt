package com.kenwang.kenapps.data.repository.parkinglist

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity
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
        val authorization = "Bear auth"
        val city = ParkingSpaceCity.Kaohsiung
        every { parkingListLocalDataSource.getParkingList(city = city) } returns emptyList() andThen parkingList
        coEvery { parkingListServerDataSource.getParkingList(authorization = authorization, parkingSpaceCity = city) } returns parkingList

        parkingListRepository.getParkingList(authorization = authorization, parkingSpaceCity = city)

        Truth
            .assertThat(parkingListLocalDataSource.getParkingList(city = city))
            .isEqualTo(parkingList)
        coVerify { parkingListServerDataSource.getParkingList(authorization = authorization, parkingSpaceCity = city) }
    }

    @Test
    fun `Test getParkingList with parkingListLocalDataSource parkingList is not empty then parkingListLocalDataSource get result`() = runTest {
        val parkingList = listOf(ParkingSpace())
        val authorization = "Bear auth"
        val city = ParkingSpaceCity.Kaohsiung
        every { parkingListLocalDataSource.getParkingList(city = city) } returns parkingList

        parkingListRepository.getParkingList(authorization = authorization, parkingSpaceCity = city)

        Truth
            .assertThat(parkingListLocalDataSource.getParkingList(city = city))
            .isEqualTo(parkingList)
        coVerify(exactly = 0) { parkingListServerDataSource.getParkingList(authorization = authorization, parkingSpaceCity = city) }
    }
}
