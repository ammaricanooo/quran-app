package com.ammaricano.quran.data.local

import android.content.Context
import android.content.SharedPreferences
import com.ammaricano.quran.data.model.AsmaulHusnaItem
import com.ammaricano.quran.data.model.BookmarkModel
import com.ammaricano.quran.data.model.DzikirItem
import com.ammaricano.quran.data.model.HaditsItem
import com.ammaricano.quran.data.model.TahlilItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class LocalRepository(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getAsmaulHusna(): List<AsmaulHusnaItem> {
        return try {
            val inputStream = context.assets.open("asmaul_husna.json")
            val reader = InputStreamReader(inputStream)
            val itemType = object : TypeToken<List<AsmaulHusnaItem>>() {}.type
            gson.fromJson(reader, itemType)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getHaditsArbain(): List<HaditsItem> {
        return try {
            val inputStream = context.assets.open("hadits_arbain.json")
            val reader = InputStreamReader(inputStream)
            val itemType = object : TypeToken<List<HaditsItem>>() {}.type
            gson.fromJson(reader, itemType)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getJuzList(): List<com.ammaricano.quran.data.model.JuzItem> {
        return try {
            val inputStream = context.assets.open("data_juz.json")
            val reader = InputStreamReader(inputStream)
            val itemType = object : TypeToken<List<com.ammaricano.quran.data.model.JuzItem>>() {}.type
            gson.fromJson(reader, itemType)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveLastRead(surahNomor: Int, surahName: String, ayatNomor: Int) {
        prefs.edit()
            .putInt("last_surah_no", surahNomor)
            .putString("last_surah_name", surahName)
            .putInt("last_ayat_no", ayatNomor)
            .apply()
    }

    fun getLastRead(): Triple<Int, String, Int>? {
        val surahNo = prefs.getInt("last_surah_no", -1)
        val surahName = prefs.getString("last_surah_name", null)
        val ayatNo = prefs.getInt("last_ayat_no", 1)
        return if (surahNo != -1 && surahName != null) {
            Triple(surahNo, surahName, ayatNo)
        } else null
    }

    fun getBookmarks(): List<BookmarkModel> {
        val json = prefs.getString("user_bookmarks", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<BookmarkModel>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun toggleBookmark(bookmark: BookmarkModel): Boolean {
        val currentList = getBookmarks().toMutableList()
        val existingIndex = currentList.indexOfFirst {
            it.surahNo == bookmark.surahNo && it.ayatNo == bookmark.ayatNo
        }

        val isAdded: Boolean
        if (existingIndex >= 0) {
            currentList.removeAt(existingIndex)
            isAdded = false
        } else {
            currentList.add(0, bookmark)
            isAdded = true
        }

        prefs.edit().putString("user_bookmarks", gson.toJson(currentList)).apply()
        return isAdded
    }

    fun isAyatBookmarked(surahNo: Int, ayatNo: Int): Boolean {
        return getBookmarks().any { it.surahNo == surahNo && it.ayatNo == ayatNo }
    }

    fun getArabicFontSize(): Float {
        return prefs.getFloat("font_size_arab", 24f)
    }

    fun setArabicFontSize(size: Float) {
        prefs.edit().putFloat("font_size_arab", size).apply()
    }

    fun getDzikirData(): List<DzikirItem> {
        return listOf(
            DzikirItem(
                id = "dzikir-0",
                type = "pagi",
                arab = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ",
                indo = "Aku berlindung kepada Allah dari godaan syaitan yang terkutuk.",
                ulang = "1x"
            ),
            DzikirItem(
                id = "dzikir-1",
                type = "pagi",
                arab = "اللَّهُ لاَ إِلَهَ إِلاَّ هُوَ الْحَيُّ الْقَيُّومُ، لاَ تَأْخُذُهُ سِنَةٌ وَلاَ نَوْمٌ، لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ، مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلاَّ بِإِذْنِهِ، يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ، وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلاَّ بِمَا شَاءَ، وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ، وَلَا يَئُودُهُ حِفْظُهُمَا، وَهُوَ الْعَلِيُّ الْعَظِيمُ",
                indo = "Allah, tidak ada ilah (yang berhak disembah) melainkan Dia, Yang Maha Hidup, Yang terus menerus mengurus makhluk-Nya...",
                ulang = "1x",
                faedah = "Membaca Ayat Kursi di pagi hari melindungi dari gangguan jin sampai petang."
            ),
            DzikirItem(
                id = "dzikir-2",
                type = "pagi",
                arab = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ قُلْ هُوَ اللَّهُ أَحَدٌ اللَّهُ الصَّمَدُ لَمْ يَلِدْ وَلَمْ يُولَدْ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                indo = "Katakanlah: Dialah Allah, Yang Maha Esa. Allah tempat meminta segala sesuatu...",
                ulang = "3x"
            ),
            DzikirItem(
                id = "dzikir-3",
                type = "pagi",
                arab = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ مِن شَرِّ مَا خَلَقَ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
                indo = "Katakanlah: Aku berlindung kepada Tuhan yang menguasai subuh...",
                ulang = "3x"
            ),
            DzikirItem(
                id = "dzikir-4",
                type = "pagi",
                arab = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ قُلْ أَعُوذُ بِرَبِّ النَّاسِ مَلِكِ النَّاسِ إِلَهِ النَّاسِ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ مِنَ الْجِنَّةِ وَالنَّاسِ",
                indo = "Katakanlah: Aku berlindung kepada Rabb manusia, Raja manusia, Sembahan manusia...",
                ulang = "3x"
            ),
            DzikirItem(
                id = "dzikir-5",
                type = "pagi",
                arab = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ...",
                indo = "Kami telah memasuki waktu pagi dan kerajaan hanya milik Allah, segala puji bagi Allah...",
                ulang = "1x"
            ),
            DzikirItem(
                id = "dzikir-petang-1",
                type = "petang",
                arab = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لاَ إِلَـهَ إِلاَّ اللهُ وَحْدَهُ لاَ شَرِيْكَ لَهُ...",
                indo = "Kami telah memasuki waktu petang dan kerajaan hanya milik Allah, segala puji bagi Allah...",
                ulang = "1x"
            ),
            DzikirItem(
                id = "dzikir-petang-2",
                type = "petang",
                arab = "اَللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوْتُ، وَإِلَيْكَ الْمَصِيْرُ",
                indo = "Ya Allah, dengan rahmat dan pertolongan-Mu kami memasuki waktu petang...",
                ulang = "1x"
            )
        )
    }

    fun getTahlilData(): List<TahlilItem> {
        return listOf(
            TahlilItem(
                id = "tahlil-1",
                title = "Niyyat Tahlil",
                arabic = "إِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى سَيِّدِنَا مُحمَّدٍ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ وَاٰلِهِ وَأَزْوَاجِهِ وَأَوْلَادِهِ وَذُرِّيَّاتِهِ الْفَــاتِحَةُ",
                latin = "Ilâ ḥaḍratin-nabiyyil-muṣṭafâ sayyidinâ Muḥammadin...",
                translation = "Kepada yang terhormat Nabi Muhammad ﷺ, segenap keluarga, istri-istri, anak-anak, dan keturunannya...",
                repeat = "1x"
            ),
            TahlilItem(
                id = "tahlil-2",
                title = "Membaca Surat Al-Ikhlas",
                arabic = "قُلْ هُوَ اللَّهُ أَحَدٌ، اللَّهُ الصَّمَدُ، لَمْ يَلِدْ وَلَمْ يُولَدْ، وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                latin = "Qul huwallâhu aḥad, allâhuṣ-ṣamad...",
                translation = "Katakanlah: Dialah Allah, Yang Maha Esa. Allah adalah tempat bergantung segala sesuatu...",
                repeat = "3x"
            ),
            TahlilItem(
                id = "tahlil-3",
                title = "Tahlil & Takbir",
                arabic = "لَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ",
                latin = "Lâ ilâha illallâhu wallâhu akbar",
                translation = "Tiada Tuhan selain Allah, dan Allah Maha Besar.",
                repeat = "1x"
            ),
            TahlilItem(
                id = "tahlil-4",
                title = "Membaca Kalimat Tahlil Inti",
                arabic = "لَا إِلٰهَ إِلَّا اللهُ",
                latin = "Lâ ilâha illallâh",
                translation = "Tiada Tuhan selain Allah.",
                repeat = "33x / 100x"
            ),
            TahlilItem(
                id = "tahlil-5",
                title = "Doa Tahlil Penutup",
                arabic = "اَللَّهُمَّ تَقَبَّلْ وَأَوْصِلْ ثَوَابَ مَا قَرَأْنَاهُ مِنْ كِتَابِكَ الْعَزِيْزِ وَمَا هَلَّلْنَا وَمَا سَبَّحْنَا...",
                latin = "Allâhumma taqabbal wa aushil tsawâba mâ qara'nâhu...",
                translation = "Ya Allah, terimalah dan sampaikanlah pahala Al-Qur'an yang kami baca, tahlil kami, tasbih kami...",
                repeat = "1x"
            )
        )
    }
}
