package com.kenwang.kenapps.data.repository.cctvlist

import com.google.gson.annotations.SerializedName

data class CctvListBean(
    @SerializedName("cctvid") val cctvId: String = "",
    @SerializedName("roadsection") val roadsection: String = "",
    @SerializedName("locationpath") val locationpath: String = "",
    @SerializedName("startlocationpoint") val startlocationpoint: String = "",
    @SerializedName("endlocationpoint") val endlocationpoint: String = "",
    @SerializedName("px") val longitude: Double = 0.0,
    @SerializedName("py") val latitude: Double = 0.0,
)
