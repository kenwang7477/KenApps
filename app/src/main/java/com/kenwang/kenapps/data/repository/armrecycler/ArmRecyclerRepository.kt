package com.kenwang.kenapps.data.repository.armrecycler

import com.kenwang.kenapps.data.model.ArmRecycler

class ArmRecyclerRepository(
    private val armRecyclerClient: ArmRecyclerClient
) {

    private var armRecyclerList = emptyList<ArmRecycler>()

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        return armRecyclerList.ifEmpty {
            armRecyclerList = armRecyclerClient.getArmRecyclerList()
            armRecyclerList
        }
    }
}
