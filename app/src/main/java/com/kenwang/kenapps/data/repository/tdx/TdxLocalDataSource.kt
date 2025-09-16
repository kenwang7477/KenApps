package com.kenwang.kenapps.data.repository.tdx

class TdxLocalDataSource {
    private var token: TdxTokenResponse = TdxTokenResponse()
    private var getTokenTime: Long = 0

    fun putToken(token: TdxTokenResponse) {
        getTokenTime = System.currentTimeMillis()
        this.token = token
    }

    fun getAuthorization(): String {
        return "${token.tokenType} ${token.accessToken}"
    }

    fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() - getTokenTime >= token.expiresIn * 1000
    }
}
