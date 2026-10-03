package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class SurahListResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<SurahItem>
)

data class SurahItem(
    @SerializedName("nomor") val nomor: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("namaLatin") val namaLatin: String,
    @SerializedName("jumlahAyat") val jumlahAyat: Int,
    @SerializedName("tempatTurun") val tempatTurun: String,
    @SerializedName("arti") val arti: String,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("audioFull") val audioFull: Map<String, String>?
)
