package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class HaditsItem(
    @SerializedName("number")
    val no: Int,
    @SerializedName("judul")
    val judul: String = "",
    @SerializedName("arab")
    val arab: String = "",
    @SerializedName("id")
    val indo: String = "",
    @SerializedName("source")
    val source: String? = null
)
