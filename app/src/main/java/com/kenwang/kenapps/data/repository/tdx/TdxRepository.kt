package com.kenwang.kenapps.data.repository.tdx

class TdxRepository(
    private val tdxServerDataSource: TdxServerDataSource,
    private val tdxLocalDataSource: TdxLocalDataSource
) {

    suspend fun getAuthorization(): String {
        if (tdxLocalDataSource.isTokenExpired()) {
            val token = tdxServerDataSource.getToken()
            tdxLocalDataSource.putToken(token = token)
        }
        return tdxLocalDataSource.getAuthorization()
    }
}
