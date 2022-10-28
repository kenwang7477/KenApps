package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ParkingSpaceBean(
    @SerializedName("行政區") val area: String = "",
    @SerializedName("型式") val type: String = "",
    @SerializedName("場名") val name: String = "",
    @SerializedName("位置") val address: String = "",
    @SerializedName("經度") val longitude: Double = 0.0,
    @SerializedName("緯度") val latitude: Double = 0.0,
    @SerializedName("大型車") val largeCarCount: Int = 0,
    @SerializedName("小型車") val normalCarCount: Int = 0,
    @SerializedName("機慢車") val bicycleCount: Int = 0,
    @SerializedName("收費標準") val charges: String = "",
    @SerializedName("管理者資訊") val information: String = ""
)

@Parcelize
data class ParkingSpace(
    val area: String = "",
    val type: String = "",
    val name: String = "",
    val address: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val largeCarCount: Int = 0,
    val normalCarCount: Int = 0,
    val bicycleCount: Int = 0,
    val charges: String = "",
    val information: String = ""
) : Parcelable {

    object NavigationType : NavType<ParkingSpace>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ParkingSpace? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): ParkingSpace {
            return Gson().fromJson(value, ParkingSpace::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: ParkingSpace) {
            bundle.putParcelable(key, value)
        }
    }
}
