package com.kenwang.kenapps.data.repository.armrecycler

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.ArmRecycler

class ArmRecyclerClient(
    private val armRecyclerService: ArmRecyclerService,
    private val gson: Gson
) {

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        return armRecyclerService.getArmRecyclerList().body()?.mapNotNull {
            try {
                val armRecycler = gson.fromJson(it, ArmRecycler::class.java)
                if (armRecycler.address.isEmpty()) {
                    null
                } else {
                    armRecycler
                }
            } catch(e: Exception) {
                null
            }
        } ?: emptyList()
    }
}
