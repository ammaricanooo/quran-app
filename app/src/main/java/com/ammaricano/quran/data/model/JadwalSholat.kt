package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class ShalatResponse(
    @SerializedName("code") val code: Int = 200,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: ShalatData? = null
)

data class ShalatData(
    @SerializedName("provinsi") val provinsi: String = "",
    @SerializedName("kabkota") val kabkota: String = "",
    @SerializedName("bulan") val bulan: Int = 1,
    @SerializedName("tahun") val tahun: Int = 2026,
    @SerializedName("jadwal") val jadwal: List<ShalatJadwalItem> = emptyList()
)

data class ShalatJadwalItem(
    @SerializedName("tanggal") val tanggal: Int = 1,
    @SerializedName("tanggal_lengkap") val tanggalLengkap: String? = null,
    @SerializedName("hari") val hari: String? = null,
    @SerializedName("imsak") val imsak: String = "04:15",
    @SerializedName("subuh") val subuh: String = "04:25",
    @SerializedName("terbit") val terbit: String = "05:40",
    @SerializedName("dhuha") val dhuha: String = "06:05",
    @SerializedName("dzuhur") val dzuhur: String = "11:55",
    @SerializedName("ashar") val ashar: String = "15:05",
    @SerializedName("maghrib") val maghrib: String = "17:58",
    @SerializedName("isya") val isya: String = "19:08"
)

data class CityOption(
    @SerializedName("kota") val kota: String = "Kota Jakarta",
    @SerializedName("provinsi") val provinsi: String = "DKI Jakarta",
    @SerializedName("label") val label: String = "Jakarta, DKI Jakarta"
)
