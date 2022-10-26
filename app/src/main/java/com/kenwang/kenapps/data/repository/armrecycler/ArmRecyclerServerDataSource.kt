package com.kenwang.kenapps.data.repository.armrecycler

import com.kenwang.kenapps.data.model.ArmRecycler

class ArmRecyclerServerDataSource(
    private val armRecyclerClient: ArmRecyclerClient
) {

    suspend fun getArmRecyclerList(): List<ArmRecycler> = armRecyclerClient.getArmRecyclerList()
}
