package com.kenwang.kenapps.data.repository.armrecycler

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.model.ArmRecyclerBean

class ArmRecyclerClient(
    private val armRecyclerService: ArmRecyclerService,
    private val armRecyclerMapper: ArmRecyclerMapper,
    private val gson: Gson
) {

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        return armRecyclerService.getArmRecyclerList().body()?.mapNotNull {
            try {
                val bean = gson.fromJson(it, ArmRecyclerBean::class.java)
                if (bean.address.isBlank()) {
                    null
                } else {
                    armRecyclerMapper.toArmRecycler(bean)
                }
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
    }
}

class ArmRecyclerMapper() {

    fun toArmRecycler(armRecyclerBean: ArmRecyclerBean): ArmRecycler {
        return ArmRecycler(
            name = armRecyclerBean.name,
            address = armRecyclerBean.address,
            count = armRecyclerBean.count,
            area = armRecyclerBean.area,
            time = armRecyclerBean.time,
            recycleItem = armRecyclerBean.recycleItem,
            position = armRecyclerBean.position
        )
    }
}
