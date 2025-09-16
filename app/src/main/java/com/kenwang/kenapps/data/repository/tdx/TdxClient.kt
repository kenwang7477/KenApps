package com.kenwang.kenapps.data.repository.tdx

import com.kenwang.kenapps.data.repository.base.APIClientBase
import com.kenwang.kenapps.data.repository.base.APIClientBaseImpl

class TdxClient(
    private val tdxService: TdxService
) : APIClientBase by APIClientBaseImpl() {

    suspend fun getToken(): TdxTokenResponse {
        val result = tdxService.getToken()
        return result.getOrNull() ?: TdxTokenResponse()
    }
}
