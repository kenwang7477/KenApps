package com.kenwang.kenapps.domain.usecase.parkinglist

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kenwang.kenapps.data.api.APIException
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.repository.parkinglist.ParkingListRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetParkingListUseCase @Inject constructor(
    private val parkingListRepository: ParkingListRepository
) {

    var currentLatLng: LatLng? = null

    operator fun invoke() = flow {
        try {
            val parkingList = parkingListRepository.getParkingList()
            if (parkingList.isEmpty()) {
                emit(Result.Empty)
            } else {
                currentLatLng?.let {
                    val sortedList = parkingList.sortedBy { truck ->
                        val to = LatLng(truck.latitude, truck.longitude)
                        SphericalUtil.computeDistanceBetween(currentLatLng, to)
                    }
                    emit(Result.Success(sortedList))
                } ?: emit(Result.Success(parkingList))
            }
        } catch (e: APIException) {
            emit(Result.Error(e))
        }
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val list: List<ParkingSpace>) : Result()
        data class Error(val exception: APIException) : Result()
    }
}
