package com.kenwang.kenapps.domain.usecase.garbagetrucklist

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGarbageTruckListUseCase @Inject constructor(
    private val garbageTruckRepository: GarbageTruckRepository
) {

    var currentLatLng: LatLng? = null
    var forceUpdate: Boolean = false

    operator fun invoke() = flow {
        val garbageTruckList = garbageTruckRepository.getTrucks(forceUpdate)
        if (garbageTruckList.isEmpty()) {
            emit(Result.Empty)
        } else {
            currentLatLng?.let {
                val sortedList = garbageTruckList.sortedBy { truck ->
                    val to = LatLng(truck.latitude, truck.longitude)
                    SphericalUtil.computeDistanceBetween(currentLatLng, to)
                }
                emit(Result.Success(sortedList))
            } ?: emit(Result.Success(garbageTruckList))
        }
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val list: List<GarbageTruck>) : Result()
    }
}
