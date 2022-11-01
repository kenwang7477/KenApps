package com.kenwang.kenapps.ui

sealed class Screens(val route: String) {
    object Main : Screens("Main")
    object ParkingList : Screens("ParkingList")
    object ParkingMap : Screens("ParkingMapScreen") {
        const val argParkingSpace = "ParkingSpace"
    }
    object GarbageTruckList : Screens("GarbageTruckList")
    object GarbageTruckMap : Screens("GarbageTruckMap") {
        const val argGarbageTruck = "GarbageTruck"
    }
    object TvProgramList : Screens("TvProgramList")
    object ArmRecyclerList : Screens("ArmRecyclerList")
    object CctvList : Screens("CctvList")
    object CctvMap : Screens("CctvMap") {
        const val argCctvMonitor = "CctvMonitor"
    }
    object Setting : Screens("Setting")
}
