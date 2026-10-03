package com.ammaricano.quran.data.model

data class TahlilItem(
    val id: String,
    val title: String,
    val arabic: String,
    val latin: String,
    val translation: String,
    val repeat: String? = null
)
