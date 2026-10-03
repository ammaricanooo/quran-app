package com.ammaricano.quran.data.model

import com.google.gson.annotations.SerializedName

data class HaditsItem(
    @SerializedName("no") val no: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("arab") val arab: String,
    @SerializedName("indo") val indo: String
)

data class TahlilItem(
    val id: String,
    val title: String,
    val arabic: String,
    val latin: String,
    val translation: String,
    val repeat: String? = null
)

data class BookmarkModel(
    val surahNo: Int,
    val surahName: String,
    val ayatNo: Int,
    val arabSnippet: String,
    val indoSnippet: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class JuzItem(
    @SerializedName("number") val number: Int,
    @SerializedName("name") val name: String,
    @SerializedName("name_start_id") val nameStartId: String,
    @SerializedName("name_end_id") val nameEndId: String,
    @SerializedName("verse_start") val verseStart: String,
    @SerializedName("verse_end") val verseEnd: String,
    @SerializedName("surah_id_start") val surahIdStart: String,
    @SerializedName("surah_id_end") val surahIdEnd: String,
    @SerializedName("ayat_arab") val ayatArab: String,
    @SerializedName("ayat_indo") val ayatIndo: String
)

