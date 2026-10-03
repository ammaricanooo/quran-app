package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class TafsirResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TafsirData
)

data class TafsirData(
    @SerializedName("nomor") val nomor: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("namaLatin") val namaLatin: String,
    @SerializedName("tafsir") val tafsir: List<TafsirItem>
)

data class TafsirItem(
    @SerializedName("ayat") val ayat: Int,
    @SerializedName("teks") val teks: String
)
