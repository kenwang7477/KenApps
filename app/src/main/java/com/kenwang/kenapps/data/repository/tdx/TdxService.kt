package com.kenwang.kenapps.data.repository.tdx

import com.kenwang.kenapps.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

class TdxService(private val httpClient: HttpClient) {

    suspend fun getToken(): Result<TdxTokenResponse> {
        return runCatching {
            httpClient
                .post("auth/realms/TDXConnect/protocol/openid-connect/token") {
                    setBody(
                        FormDataContent(
                            Parameters.build {
                                append("grant_type", "client_credentials")
                                append("client_id", BuildConfig.TDX_CLIENT_ID)
                                append("client_secret", BuildConfig.TDX_CLIENT_SECRET)
                            }
                        )
                    )
                }
                .body()
        }
    }
}
