package com.kenwang.kenapps.data.repository.cctvlist

import com.kenwang.kenapps.data.model.CctvMonitor

class CctvListServerDataSource(
    private val cctvListClient: CctvListClient
) {

    suspend fun getCctvList(): List<CctvMonitor> = cctvListClient.getCctvList()
}
