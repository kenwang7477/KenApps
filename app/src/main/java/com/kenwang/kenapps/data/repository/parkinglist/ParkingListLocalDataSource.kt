package com.kenwang.kenapps.data.repository.parkinglist

import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceCity

class ParkingListLocalDataSource {

    private var city: ParkingSpaceCity? = null
    private var parkingList: List<ParkingSpace> = emptyList()

    fun putParkingList(city: ParkingSpaceCity, parkingList: List<ParkingSpace>) {
        this.city = city
        this.parkingList = parkingList
    }

    fun getParkingList(city: ParkingSpaceCity): List<ParkingSpace> {
        return if (this.city == city) {
            parkingList
        } else {
            emptyList()
        }
    }
}
