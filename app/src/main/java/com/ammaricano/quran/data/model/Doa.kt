package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class DoaItem(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("grup") val grup: String?,
    @SerializedName("ar") val ar: String,
    @SerializedName("tr") val tr: String?,
    @SerializedName("idn") val idn: String,
    @SerializedName("tentang") val tentang: String?
)
