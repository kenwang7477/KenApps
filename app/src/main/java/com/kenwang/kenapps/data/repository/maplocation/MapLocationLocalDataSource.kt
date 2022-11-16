package com.kenwang.kenapps.data.repository.maplocation

import com.kenwang.kenapps.data.database.maplocation.MapLocationDao
import com.kenwang.kenapps.data.database.maplocation.MapLocationEntity
import com.kenwang.kenapps.data.model.MapLocation
import kotlinx.coroutines.flow.map

class MapLocationLocalDataSource(
    private val mapLocationDao: MapLocationDao,
    private val mapLocationMapper: MapLocationMapper
) {

    fun getMapLocationList() = mapLocationDao.getMapLocationList().map { list ->
        list.map { mapLocationMapper.toMapLocation(it) }
    }

    suspend fun insertMapLocationEntity(mapLocation: MapLocation) {
        mapLocationDao.insert(mapLocationMapper.toMapLocationEntity(mapLocation))
    }

    suspend fun deleteMapLocationEntity(mapLocation: MapLocation) {
        mapLocationDao.delete(mapLocationMapper.toMapLocationEntity(mapLocation))
    }
}

class MapLocationMapper {

    fun toMapLocation(mapLocationEntity: MapLocationEntity): MapLocation {
        return MapLocation(
            timestamp = mapLocationEntity.timestamp,
            title = mapLocationEntity.title,
            description =  mapLocationEntity.description,
            longitude = mapLocationEntity.longitude,
            latitude = mapLocationEntity.latitude
        )
    }

    fun toMapLocationEntity(mapLocation: MapLocation): MapLocationEntity {
        return MapLocationEntity(
            timestamp = mapLocation.timestamp,
            title = mapLocation.title,
            description =  mapLocation.description,
            longitude = mapLocation.longitude,
            latitude = mapLocation.latitude
        )
    }
}
