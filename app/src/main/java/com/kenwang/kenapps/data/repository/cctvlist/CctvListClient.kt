package com.kenwang.kenapps.data.repository.cctvlist

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class CctvListClient(
    private val cctvListService: CctvListService,
    private val gson: Gson
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getCctvList(): List<CctvMonitor> {
        val response = checkAPIResponse { cctvListService.getCctvList() }
        return response.body()?.mapNotNull {
            try {
                val bean = gson.fromJson(it, CctvListBean::class.java)
                CctvMonitorMapper.toCctvMonitor(bean)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }
}

object CctvMonitorMapper {

    fun toCctvMonitor(response: CctvListBean): CctvMonitor {
        return CctvMonitor(
            cctvId = response.cctvId,
            roadsection = response.roadsection,
            locationpath = response.locationpath,
            startlocationpoint = response.startlocationpoint,
            endlocationpoint = response.endlocationpoint,
            longitude = response.longitude,
            latitude = response.latitude
        )
    }
}
