package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace

class ParkingListRepository(
    private val parkingListServerDataSource: ParkingListServerDataSource,
    private val parkingListLocalDataSource: ParkingListLocalDataSource
) {

    suspend fun getParkingList(): List<ParkingSpace> {
        return parkingListLocalDataSource.parkingList.ifEmpty {
            parkingListLocalDataSource.parkingList = parkingListServerDataSource.getParkingList()
            parkingListLocalDataSource.parkingList
        }
    }
}
