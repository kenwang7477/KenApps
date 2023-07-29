package com.kenwang.kenapps.data.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ArmRecyclerBean(
    @SerialName("單位名稱") val name: String = "",
    @SerialName("地址") val address: String = "",
    @SerialName("數量") val count: String = "",
    @SerialName("行政區") val area: String = "",
    @SerialName("使用時間") val time: String = "",
    @SerialName("回收項目") val recycleItem: String = "",
    @SerialName("詳細位置") val position: String = ""
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

        @OptIn(ExperimentalSerializationApi::class)
        private val json = Json {
            isLenient = true
            explicitNulls = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        override fun get(bundle: Bundle, key: String): ArmRecycler? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): ArmRecycler {
            return json.decodeFromString(value)
        }

        override fun put(bundle: Bundle, key: String, value: ArmRecycler) {
            bundle.putParcelable(key, value)
        }
    }
}
