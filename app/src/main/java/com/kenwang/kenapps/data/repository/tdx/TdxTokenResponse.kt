package com.kenwang.kenapps.data.repository.tdx

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TdxTokenResponse(
    @SerialName("access_token") val accessToken: String = "",
    @SerialName("expires_in") val expiresIn: Long = 0,
    @SerialName("token_type") val tokenType: String = ""
)
