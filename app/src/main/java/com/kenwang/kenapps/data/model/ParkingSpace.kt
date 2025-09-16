package com.kenwang.kenapps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParkingSpaceBean(
    @SerialName("CarParks") val carParks: List<ParkingSpaceDetailBean> = emptyList()
) {

    @Serializable
    data class ParkingSpaceDetailBean(
        @SerialName("CarParkName") val parkName: ParkNameBean = ParkNameBean(),
        @SerialName("Description") val description: String = "",
        @SerialName("CarParkPosition") val position: ParkPositionBean = ParkPositionBean(),
        @SerialName("Address") val address: String = "",
        @SerialName("EmergencyPhone") val emergencyPhone: String = "",
        @SerialName("FareDescription") val fareDescription: String = "",
        @SerialName("IsPublic") val isPublic: Int = 0
    )

    @Serializable
    data class ParkNameBean(
        @SerialName("Zh_tw") val twName: String? = ""
    )

    @Serializable
    data class ParkPositionBean(
        @SerialName("PositionLat") val latitude: Double = 0.0,
        @SerialName("PositionLon") val longitude: Double = 0.0
    )
}

@Serializable
data class ParkingSpace(
    val name: String = "",
    val description: String = "",
    val fareDescription: String = "",
    val address: String = "",
    val emergencyPhone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isPublic: Boolean = false
)

enum class ParkingSpaceCity(val cityName: String) {
    Taipei("Taipei"),
    Taoyuan("Taoyuan"),
    Taichung("Taichung"),
    Tainan("Tainan"),
    Kaohsiung("Kaohsiung")
}
