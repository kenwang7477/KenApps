package com.kenwang.kenapps.domain.usecase.cctvlist

import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.data.repository.cctvlist.CctvListRepository
import javax.inject.Inject

class GetCctvListUseCase @Inject constructor(
    private val cctvListRepository: CctvListRepository
) {

    private var keyword: String = ""

    suspend operator fun invoke(): Result {
        val cctvList = cctvListRepository.getCctvList().filter {
            it.roadsection.contains(keyword)
        }
        return if (cctvList.isEmpty()) {
            Result.Empty
        } else {
            Result.Success(cctvList)
        }
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val cctvList: List<CctvMonitor>) : Result()
    }
}
