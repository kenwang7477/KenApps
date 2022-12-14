package com.kenwang.kenapps.data.repository.parkinglist

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.ParkingSpaceBean

class ParkingListClient(
    private val parkingListService: ParkingListService,
    private val parkingSpaceMapper: ParkingSpaceMapper,
    private val gson: Gson
) {

    suspend fun getParkingList(): List<ParkingSpace> {
        return parkingListService.getParkingList().body()?.mapNotNull {
            try {
                val bean = gson.fromJson(it, ParkingSpaceBean::class.java)
                parkingSpaceMapper.toParkingSpace(bean)
            } catch(e: Exception) {
                null
            }
        } ?: emptyList()
    }
}

class ParkingSpaceMapper {

    fun toParkingSpace(parkingSpaceBean: ParkingSpaceBean): ParkingSpace {
        return ParkingSpace(
            area = parkingSpaceBean.area,
            type = parkingSpaceBean.type,
            name = parkingSpaceBean.name,
            address = parkingSpaceBean.address,
            longitude = parkingSpaceBean.longitude,
            latitude = parkingSpaceBean.latitude,
            largeCarCount = parkingSpaceBean.largeCarCount,
            normalCarCount = parkingSpaceBean.normalCarCount,
            bicycleCount = parkingSpaceBean.bicycleCount,
            charges = parkingSpaceBean.charges,
            information = parkingSpaceBean.information
        )
    }
}
