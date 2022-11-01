package com.kenwang.kenapps.extensions

import android.net.Uri
import androidx.navigation.NavController
import com.google.gson.Gson
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.ui.Screens

fun NavController.toParkingList() = navigate(Screens.ParkingList.route)

fun NavController.toParkingMap(
    parkingSpace: ParkingSpace
) {
    val parkingJson = Uri.encode(Gson().toJson(parkingSpace))
    navigate("${Screens.ParkingMap.route}/$parkingJson")
}

fun NavController.toGarbageTruckList() = navigate(Screens.GarbageTruckList.route)

fun NavController.toGarbageTruckMap(garbageTruck: GarbageTruck) {
    val truckJson = Gson().toJson(garbageTruck)
    navigate("${Screens.GarbageTruckMap.route}/$truckJson")
}

fun NavController.toTvProgramList() = navigate(Screens.TvProgramList.route)

fun NavController.toArmRecyclerList() = navigate(Screens.ArmRecyclerList.route)

fun NavController.toCctvList() = navigate(Screens.CctvList.route)

fun NavController.toCctvMap(cctvMonitor: CctvMonitor) {
    val cctv = Gson().toJson(cctvMonitor)
    navigate("${Screens.CctvMap.route}/$cctv")
}

fun NavController.toSetting() {
    navigate(Screens.Setting.route)
}
