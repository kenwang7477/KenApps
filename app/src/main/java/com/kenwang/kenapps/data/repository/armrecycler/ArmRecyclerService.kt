package com.kenwang.kenapps.data.repository.armrecycler

import com.kenwang.kenapps.data.model.ArmRecyclerBean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArmRecyclerService(private val httpClient: HttpClient) {

    // https://data.gov.tw/dataset/86420
    suspend fun getArmRecyclerList(): Result<List<ArmRecyclerBean>> {
        return runCatching {
            httpClient
                .get("dq_download_json.php?nid=86420&md5_url=7ddfe00e6779090ba10ec9e10b1d6aea")
                .body()
        }
    }
}
