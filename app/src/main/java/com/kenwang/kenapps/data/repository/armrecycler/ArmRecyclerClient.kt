package com.kenwang.kenapps.data.repository.armrecycler

import com.google.gson.Gson
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.model.ArmRecyclerBean
import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class ArmRecyclerClient(
    private val armRecyclerService: ArmRecyclerService,
    private val armRecyclerMapper: ArmRecyclerMapper,
    private val gson: Gson
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getArmRecyclerList(): List<ArmRecycler> {
        val response = checkAPIResponse { armRecyclerService.getArmRecyclerList() }
        return response.body()?.mapNotNull {
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
