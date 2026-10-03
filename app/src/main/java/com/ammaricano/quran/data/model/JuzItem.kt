package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class JuzItem(
    @SerializedName("number")
    val number: Int = 1,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("name_start_id")
    val nameStartId: String = "",
    @SerializedName("name_end_id")
    val nameEndId: String = "",
    @SerializedName("verse_start")
    val verseStart: String = "",
    @SerializedName("verse_end")
    val verseEnd: String = "",
    @SerializedName("surah_id_start")
    val surahIdStart: String = "1",
    @SerializedName("surah_id_end")
    val surahIdEnd: String = "1",
    @SerializedName("ayat_arab")
    val ayatArab: String = "",
    @SerializedName("ayat_indo")
    val ayatIndo: String = ""
)
