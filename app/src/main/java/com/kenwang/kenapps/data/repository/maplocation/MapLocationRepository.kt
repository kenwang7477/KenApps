package com.kenwang.kenapps.data.repository.maplocation

import com.kenwang.kenapps.data.model.MapLocation

class MapLocationRepository(
    private val mapLocationLocalDataSource: MapLocationLocalDataSource
) {

    fun getMapLocationList() = mapLocationLocalDataSource.getMapLocationList()

    suspend fun insertMapLocationEntity(mapLocation: MapLocation) {
        mapLocationLocalDataSource.insertMapLocationEntity(mapLocation)
    }

    suspend fun deleteMapLocationEntity(mapLocation: MapLocation) {
        mapLocationLocalDataSource.deleteMapLocationEntity(mapLocation)
    }
}
