package com.ammaricano.quran.data.model

/**
 * Model for user data stored in Firestore (mirrors the web app's UserData)
 */
data class UserData(
    val displayName: String? = null,
    val lastRead: LastReadData? = null,
    val bookmarks: List<BookmarkItem> = emptyList(),
    val asmaulHusnaMemorized: List<Int> = emptyList(),
    val settings: UserSettings? = null
)

data class LastReadData(
    val surahNo: Int = 0,
    val surahName: String = "",
    val ayatNo: Int = 1,
    val updatedAt: String? = null
)

data class BookmarkItem(
    val id: String = "",
    val category: String = "",
    val title: String = "",
    val subtitle: String? = null,
    val teksArab: String? = null,
    val teksLatin: String? = null,
    val teksIndo: String? = null,
    val href: String? = null,
    val createdAt: String? = null
)

data class UserSettings(
    val fontSizeArab: Int = 3,
    val fontSizeLatin: Int = 3,
    val fontSizeTranslation: Int = 3,
    val showLatin: Boolean = true,
    val showTranslation: Boolean = true
)

/**
 * Robithoh / Al-Ma'tsurat item
 */
data class RobithohItem(
    val id: Int,
    val title: String,
    val arab: String,
    val latin: String,
    val indo: String,
    val ulang: String,
    val faedah: String,
    val sumber: String
)

/**
 * Kultum (Kuliah Tujuh Menit) data model
 */
data class KultumItem(
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val summary: String,
    val muqaddimah: KultumMuqaddimah,
    val pembukaan: String,
    val points: List<KultumPoint>,
    val dalilQuran: KultumDalilQuran,
    val dalilHadits: KultumDalilHadits,
    val kesimpulan: List<String>,
    val doaPenutup: KultumDoaPenutup
)

data class KultumMuqaddimah(
    val arab: String,
    val latin: String,
    val arti: String
)

data class KultumPoint(
    val heading: String,
    val body: String
)

data class KultumDalilQuran(
    val ayatArab: String,
    val latin: String,
    val arti: String,
    val surah: String,
    val nomorAyat: String
)

data class KultumDalilHadits(
    val haditsArab: String,
    val arti: String,
    val perawi: String
)

data class KultumDoaPenutup(
    val arab: String,
    val latin: String,
    val arti: String
)

/**
 * Article data model
 */
data class ArticleItem(
    val id: Int,
    val title: String,
    val slug: String,
    val image: ArticleImage? = null,
    val category: ArticleCategory? = null,
    val published_at: String = ""
)

data class ArticleImage(
    val thumbnail: String? = null,
    val medium: String? = null,
    val full: String? = null,
    val caption: String? = null
)

data class ArticleCategory(
    val id: Int? = null,
    val name: String = ""
)

/**
 * Maulid data model
 */
data class MaulidBook(
    val name: String,
    val slug: String,
    val count: Int
)

data class MaulidReading(
    val id: Int,
    val order: Int,
    val type: Int,
    val arabic: String,
    val transliteration: String,
    val translate: String
)

data class MaulidDetail(
    val name: String,
    val slug: String,
    val readings: List<MaulidReading>
)
