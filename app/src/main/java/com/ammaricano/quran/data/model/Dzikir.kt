package com.ammaricano.quran.data.model

data class DzikirItem(
    val id: String,
    val type: String, // "pagi" or "petang"
    val arab: String,
    val latin: String? = null,
    val indo: String,
    val ulang: String,
    val faedah: String? = null
)
