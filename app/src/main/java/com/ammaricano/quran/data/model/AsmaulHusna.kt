package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class AsmaulHusnaItem(
    @SerializedName("id") val id: Int,
    @SerializedName("arab") val arab: String,
    @SerializedName("latin") val latin: String,
    @SerializedName("indo") val indo: String
)
