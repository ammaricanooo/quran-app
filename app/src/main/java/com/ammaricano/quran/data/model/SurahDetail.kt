package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class SurahDetailResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SurahDetailData
)

data class SurahDetailData(
    @SerializedName("nomor") val nomor: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("namaLatin") val namaLatin: String,
    @SerializedName("jumlahAyat") val jumlahAyat: Int,
    @SerializedName("tempatTurun") val tempatTurun: String,
    @SerializedName("arti") val arti: String,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("audioFull") val audioFull: Map<String, String>?,
    @SerializedName("ayat") val ayat: List<AyatItem>
)

data class AyatItem(
    @SerializedName("nomorAyat") val nomorAyat: Int,
    @SerializedName("teksArab") val teksArab: String,
    @SerializedName("teksLatin") val teksLatin: String,
    @SerializedName("teksIndonesia") val teksIndonesia: String,
    @SerializedName("audio") val audio: Map<String, String>?
)
