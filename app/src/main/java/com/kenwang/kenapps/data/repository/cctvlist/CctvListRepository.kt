package com.kenwang.kenapps.data.repository.cctvlist

import com.kenwang.kenapps.data.model.CctvMonitor

class CctvListRepository(
    private val cctvListServerDataSource: CctvListServerDataSource,
    private val cctvListLocalDataSource: CctvListLocalDataSource
) {

    suspend fun getCctvList(): List<CctvMonitor> {
        return cctvListLocalDataSource.cctvList.ifEmpty {
            cctvListLocalDataSource.cctvList = cctvListServerDataSource.getCctvList()
            cctvListLocalDataSource.cctvList
        }
    }
}
