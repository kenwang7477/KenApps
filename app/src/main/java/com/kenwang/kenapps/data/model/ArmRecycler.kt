package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArmRecyclerBean(
    @SerializedName("單位名稱") val name: String = "",
    @SerializedName("地址") val address: String = "",
    @SerializedName("數量") val count: String = "",
    @SerializedName("行政區") val area: String = "",
    @SerializedName("使用時間") val time: String = "",
    @SerializedName("回收項目") val recycleItem: String = "",
    @SerializedName("詳細位置") val position: String = ""
)

@Parcelize
data class ArmRecycler(
    val name: String = "",
    val address: String = "",
    val count: String = "",
    val area: String = "",
    val time: String = "",
    val recycleItem: String = "",
    val position: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
) : Parcelable {

    object NavigationType : NavType<ArmRecycler>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): ArmRecycler? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): ArmRecycler {
            return Gson().fromJson(value, ArmRecycler::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: ArmRecycler) {
            bundle.putParcelable(key, value)
        }
    }
}
