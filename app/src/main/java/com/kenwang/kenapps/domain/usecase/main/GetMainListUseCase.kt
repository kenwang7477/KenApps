package com.kenwang.kenapps.domain.usecase.main

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMainListUseCase @Inject constructor() {

    operator fun invoke() = flow {
        val items = listOf(
            MainListItem.ParkingMap,
            MainListItem.GarbageTruckMap,
            MainListItem.TvProgramList,
            MainListItem.ArmRecyclerMap,
            MainListItem.CctvList,
            MainListItem.MapLocation,
            MainListItem.ChatGPT
        )
        emit(Result.Success(items))
    }

    sealed class Result {
        data class Success(val list: List<MainListItem>) : Result()
    }
}

enum class MainListItem {
    ParkingMap,
    GarbageTruckMap,
    TvProgramList,
    ArmRecyclerMap,
    CctvList,
    MapLocation,
    ChatGPT
}
