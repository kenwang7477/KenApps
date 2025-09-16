package com.kenwang.kenapps.data.repository.tdx

class TdxServerDataSource(private val tdxClient: TdxClient) {

    suspend fun getToken(): TdxTokenResponse {
        return tdxClient.getToken()
    }
}
