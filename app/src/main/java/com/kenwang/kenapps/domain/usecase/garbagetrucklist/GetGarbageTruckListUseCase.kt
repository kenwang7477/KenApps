package com.kenwang.kenapps.domain.usecase.garbagetrucklist

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.kenwang.kenapps.data.api.APIException
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.repository.garbagetruck.GarbageTruckRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGarbageTruckListUseCase @Inject constructor(
    private val garbageTruckRepository: GarbageTruckRepository
) {

    operator fun invoke(currentLatLng: LatLng? = null, forceUpdate: Boolean = false) = flow {
        try {
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
        } catch (e: APIException) {
            emit(Result.Error(e))
        }
    }

    sealed class Result {
        object Empty : Result()
        data class Success(val list: List<GarbageTruck>) : Result()
        data class Error(val exception: APIException) : Result()
    }
}
