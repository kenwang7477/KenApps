package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity

class ParkingListRepository(
    private val parkingListServerDataSource: ParkingListServerDataSource,
    private val parkingListLocalDataSource: ParkingListLocalDataSource
) {

    suspend fun getParkingList(
        authorization: String,
        parkingSpaceCity: ParkingSpaceCity
    ): List<ParkingSpace> {
        return parkingListLocalDataSource.getParkingList(city = parkingSpaceCity).ifEmpty {
            val parkingList = parkingListServerDataSource.getParkingList(
                authorization = authorization,
                parkingSpaceCity = parkingSpaceCity
            )
            parkingListLocalDataSource.putParkingList(city = parkingSpaceCity, parkingList = parkingList)
            parkingList
        }
    }
}
