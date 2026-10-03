package com.ammaricano.quran.data.model

data class BookmarkModel(
    val surahNo: Int,
    val surahName: String,
    val ayatNo: Int,
    val arabSnippet: String,
    val indoSnippet: String
)
