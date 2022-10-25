package com.kenwang.kenapps.data.repository.cctvlist

import com.kenwang.kenapps.data.model.CctvMonitor

class CctvListRepository(
    private val cctvListClient: CctvListClient
) {

    private var cctvList = emptyList<CctvMonitor>()

    suspend fun getCctvList(): List<CctvMonitor> {
        return cctvList.ifEmpty {
            cctvList = cctvListClient.getCctvList()
            cctvList
        }
    }
}
