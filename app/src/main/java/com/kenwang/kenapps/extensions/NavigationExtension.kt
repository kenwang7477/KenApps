package com.kenwang.kenapps.extensions

import android.net.Uri
import androidx.navigation.NavController
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.ui.Screens
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json {
    isLenient = true
    explicitNulls = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun NavController.toParkingList() = navigate(Screens.ParkingList.route)

fun NavController.toParkingMap(
    parkingSpace: ParkingSpace
) {
    val parkingJson = Uri.encode(json.encodeToString(parkingSpace))
    navigate("${Screens.ParkingMap.route}/$parkingJson")
}

fun NavController.toGarbageTruckList() = navigate(Screens.GarbageTruckList.route)

fun NavController.toGarbageTruckMap(garbageTruck: GarbageTruck) {
    val truckJson = json.encodeToString(garbageTruck)
    navigate("${Screens.GarbageTruckMap.route}/$truckJson")
}

fun NavController.toTvProgramList() = navigate(Screens.TvProgramList.route)

fun NavController.toArmRecyclerList() = navigate(Screens.ArmRecyclerList.route)

fun NavController.toSetting() {
    navigate(Screens.Setting.route)
}

fun NavController.toMapLocationList() = navigate(Screens.MapLocationList.route)

fun NavController.toMapLocationMap(
    longitude: Double?,
    latitude: Double?
) {
    if (longitude == null || latitude == null) {
        navigate(Screens.MapLocationMap.route)
    } else {
        navigate("${Screens.MapLocationMap.route}?${Screens.MapLocationMap.argLongitude}=$longitude&${Screens.MapLocationMap.argLatitude}=$latitude")
    }
}

fun NavController.toTextToSpeech() = navigate(Screens.TextToSpeech.route)
