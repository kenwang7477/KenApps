package com.kenwang.kenapps.data.repository.armrecycler

import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.model.ArmRecyclerBean
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class ArmRecyclerClient(
    private val armRecyclerService: ArmRecyclerService,
    private val armRecyclerMapper: ArmRecyclerMapper
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        val result = armRecyclerService.getArmRecyclerList()
        return result.getOrNull()?.map { armRecyclerMapper.toArmRecycler(it) } ?: emptyList()
    }
}

class ArmRecyclerMapper {

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
