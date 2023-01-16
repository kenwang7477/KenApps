package com.kenwang.kenapps.domain.usecase.cctvlist

import com.kenwang.kenapps.data.api.APIException
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.data.repository.cctvlist.CctvListRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCctvListUseCase @Inject constructor(
    private val cctvListRepository: CctvListRepository
) {

    private var keyword: String = ""

    operator fun invoke() = flow {
        try {
            val cctvList = cctvListRepository.getCctvList().filter {
                it.roadsection.contains(keyword)
            }
            if (cctvList.isEmpty()) {
                emit(Result.Empty)
            } else {
                emit(Result.Success(cctvList))
            }
        } catch (e: APIException) {
            emit(Result.Error(e))
        }
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val cctvList: List<CctvMonitor>) : Result()
        data class Error(val exception: APIException) : Result()
    }
}
