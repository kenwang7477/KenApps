package com.kenwang.kenapps.data.repository.armrecycler

import com.kenwang.kenapps.data.model.ArmRecycler

class ArmRecyclerRepository(
    private val armRecyclerServerDataSource: ArmRecyclerServerDataSource,
    private val armRecyclerLocalDataSource: ArmRecyclerLocalDataSource
) {

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        return armRecyclerLocalDataSource.armRecyclerList.ifEmpty {
            armRecyclerLocalDataSource.armRecyclerList = armRecyclerServerDataSource.getArmRecyclerList()
            armRecyclerLocalDataSource.armRecyclerList
        }
    }
}
