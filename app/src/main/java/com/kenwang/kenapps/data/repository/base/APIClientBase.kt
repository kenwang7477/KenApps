package com.kenwang.kenapps.data.repository.base

import android.util.Log
import com.kenwang.kenapps.data.api.APIException
import com.kenwang.kenapps.utils.KenLog
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

interface APIClientBase {
    suspend fun <T> checkAPIResponse(apiCall: suspend () -> Response<T>) : Response<T>
}

class APIClientBaseImpl : APIClientBase {
    override suspend fun <T> checkAPIResponse(apiCall: suspend () -> Response<T>) : Response<T> {
        return try {
            apiCall()
        } catch (e: SocketTimeoutException) {
            KenLog.d(Log.getStackTraceString(e))
            throw APIException.APITimeoutError
        } catch (e: IOException) {
            KenLog.d(Log.getStackTraceString(e))
            throw APIException.NetworkError
        } catch (e: Exception) {
            KenLog.d(Log.getStackTraceString(e))
            throw APIException.UnknownError
        }
    }
}
