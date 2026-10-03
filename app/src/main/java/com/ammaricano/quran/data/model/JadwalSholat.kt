package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class ShalatResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ShalatData
)

data class ShalatData(
    @SerializedName("provinsi") val provinsi: String,
    @SerializedName("kabkota") val kabkota: String,
    @SerializedName("bulan") val bulan: Int,
    @SerializedName("tahun") val tahun: Int,
    @SerializedName("jadwal") val jadwal: List<ShalatJadwalItem>
)

data class ShalatJadwalItem(
    @SerializedName("tanggal") val tanggal: Int,
    @SerializedName("imsak") val imsak: String,
    @SerializedName("subuh") val subuh: String,
    @SerializedName("terbit") val terbit: String,
    @SerializedName("dhuha") val dhuha: String,
    @SerializedName("dzuhur") val dzuhur: String,
    @SerializedName("ashar") val ashar: String,
    @SerializedName("maghrib") val maghrib: String,
    @SerializedName("isya") val isya: String
)

data class CityItem(
    val kota: String,
    val provinsi: String
)
