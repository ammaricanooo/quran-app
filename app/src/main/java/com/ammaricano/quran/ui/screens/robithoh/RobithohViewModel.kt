package com.ammaricano.quran.ui.screens.robithoh

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.model.RobithohItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RobithohUiState(
    val items: List<RobithohItem> = emptyList(),
    val counts: Map<Int, Int> = emptyMap()
)

class RobithohViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(RobithohUiState())
    val uiState: StateFlow<RobithohUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val items = listOf(
            RobithohItem(
                id = 1,
                title = "QS. Ali Imran: 26",
                arab = "قُلِ اللّٰهُمَّ مٰلِكَ الْمُلْكِ تُؤْتِى الْمُلْكَ مَنْ تَشَاۤءُ وَتَنْزِعُ الْمُلْكَ مِمَّنْ تَشَاۤءُۖ وَتُعِزُّ مَنْ تَشَاۤءُ وَتُذِلُّ مَنْ تَشَاۤءُ ۗ بِيَدِكَ الْخَيْرُ ۗ اِنَّكَ عَلٰى كُلِّ شَيْءٍ قَدِيْرٌ",
                latin = "Qulillāhumma mālikal-mulki tu'til-mulka man tasyā'u wa tanzi'ul-mulka mim man tasyā'(u), wa tu'izzu man tasyā'u wa tużillu man tasyā'(u), biyadikal-khair(u), innaka 'alā kulli syai'in qadīr(un).",
                indo = "Katakanlah, \"Wahai Allah, Pemilik kekuasaan, Engkau berikan kekuasaan kepada siapa pun yang Engkau kehendaki dan Engkau cabut kekuasaan dari siapa yang Engkau kehendaki.\"",
                ulang = "1x",
                faedah = "Pengakuan ketundukan bahwa Allah memegang penuh kendali atas segala kekuasaan.",
                sumber = "QS. Ali Imran: 26"
            ),
            RobithohItem(
                id = 2,
                title = "QS. Ali Imran: 27",
                arab = "تُوْلِجُ الَّيْلَ فِى النَّهَارِ وَتُوْلِجُ النَّهَارَ فِى الَّيْلِ وَتُخْرِجُ الْحَيَّ مِنَ الْمَيِّتِ وَتُخْرِجُ الْمَيِّتَ مِنَ الْحَيِّ وَتَرْزُقُ مَنْ تَشَاۤءُ بِغَيْرِ حِسَابٍ",
                latin = "Tūlijul-laila fin-nahāri wa tūlijun-nahāra fil-laili wa tukhrijul-ḥayya minal-mayyiti wa tukhrijul-mayyita minal-ḥayyi wa tarzuqu man tasyā'u bigairi ḥisāb(in).",
                indo = "Engkau masukkan malam ke dalam siang dan Engkau masukkan siang ke dalam malam. Engkau keluarkan yang hidup dari yang mati...",
                ulang = "1x",
                faedah = "Pengakuan keagungan Allah yang menguasai rotasi waktu dan rezeki seluruh makhluk.",
                sumber = "QS. Ali Imran: 27"
            ),
            RobithohItem(
                id = 3,
                title = "Doa Petang (Iqbal Lail)",
                arab = "اللَّهُمَّ إِنَّ هَذَا إِقْبَالُ لَيْلِكَ، وَإِدْبَارُ نَهَارِكَ، وَأَصْوَاتُ دُعَاتِكَ، فَاغْفِرْ لِي",
                latin = "Allāhumma inna hādzā iqbālu lailika, wa idbāru nahārika, wa ashwātu du'ātika, faghfir lī.",
                indo = "Ya Allah, sesungguhnya inilah saat datangnya malam-Mu, dan perginya siang-Mu, serta suara-suara para penyeru-Mu, maka ampunilah aku.",
                ulang = "1x",
                faedah = "Doa ma'tsur untuk menyambut pergantian hari agar senantiasa berada dalam ampunan Allah SWT.",
                sumber = "HR. Abu Dawud & At-Tirmidzi"
            ),
            RobithohItem(
                id = 4,
                title = "Doa Rabithah",
                arab = "اَللّٰهُمَّ إِنَّكَ تَعْلَمُ أَنَّ هٰذِهِ الْقُلُوْبَ، قَدِ اجْتَمَعَتْ عَلٰى مَحَبَّتِكَ، وَالْتَقَتْ عَلٰى طَاعَتِكَ، وَتَوَحَّدَتْ عَلٰى دَعْوَتِكَ، وَتَعَاهَدَتْ عَلٰى نُصْرَةِ شَرِيْعَتِكَ",
                latin = "Allāhumma innaka ta'lamu anna hāżihil-qulūb, qadijtama'at 'alā maḥabbatik, waltaqat 'alā thā'atik...",
                indo = "Ya Allah, sesungguhnya Engkau Maha Mengetahui bahwa hati-hati ini telah berkumpul atas dasar cinta kepada-Mu, bertemu atas dasar taat pada-Mu...",
                ulang = "1x",
                faedah = "Doa Rabithah berfungsi memohon kepada Allah agar mengikat dan menyatukan hati kaum beriman.",
                sumber = "Imam Hasan Al-Banna"
            ),
            RobithohItem(
                id = 5,
                title = "Shalawat Penutup",
                arab = "اَللّٰهُمَّ اٰمِيْنَ، وَصَلِّ اللّٰهُمَّ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى اٰلِهٖ وَصَحْبِهٖ وَسَلِّمْ",
                latin = "Allāhumma āmīn, wa shallillāhumma 'alā sayyidinā Muḥammadin wa 'alā ālihī wa shaḥbihī wa sallim.",
                indo = "Ya Allah kabulkanlah. Semoga shalawat serta salam senantiasa tercurahkan kepada Nabi Muhammad SAW.",
                ulang = "1x",
                faedah = "Penutup doa yang sempurna; diiringi shalawat kepada Nabi ﷺ lebih mudah diijabah.",
                sumber = "Doa Penutup Al-Ma'tsurat"
            )
        )
        _uiState.value = RobithohUiState(items = items)
    }

    fun incrementCount(id: Int, maxStr: String) {
        val max = maxStr.replace("x", "").toIntOrNull() ?: 1
        val current = _uiState.value.counts[id] ?: 0
        if (current < max) {
            _uiState.value = _uiState.value.copy(
                counts = _uiState.value.counts + (id to current + 1)
            )
        }
    }

    fun resetCount(id: Int) {
        _uiState.value = _uiState.value.copy(
            counts = _uiState.value.counts + (id to 0)
        )
    }
}
