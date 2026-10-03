package com.ammaricano.quran.ui.screens.murottal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.model.SurahItem
import com.ammaricano.quran.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class QariItem(
    val id: String,
    val name: String,
    val style: String = "Murattal",
    val server: String,
    val totalSurah: Int = 114
)

data class MurottalUiState(
    val isLoading: Boolean = true,
    val surahs: List<SurahItem> = emptyList(),
    val filteredSurahs: List<SurahItem> = emptyList(),
    val selectedSurah: SurahItem? = null,
    val selectedQari: QariItem = QARI_LIST[0],
    val surahSearchQuery: String = "",
    val isQariPickerOpen: Boolean = false,
    val qariSearchQuery: String = "",
    val filteredQaris: List<QariItem> = QARI_LIST,
    val errorMessage: String? = null
)

// The 15 world-renowned Qaris matching lib/murottal-data.ts on Web
val QARI_LIST = listOf(
    QariItem("mishary", "Mishary Rashid Alafasy", "Murattal", "https://download.quranicaudio.com/qdc/mishari_al_afasy/murattal/"),
    QariItem("sudais", "Abdur-Rahman As-Sudais", "Murattal", "https://download.quranicaudio.com/qdc/abdurrahmaan_as_sudais/murattal/"),
    QariItem("shuraim", "Saud Al-Shuraim", "Murattal", "https://server7.mp3quran.net/shur/"),
    QariItem("maher", "Maher Al-Muaiqly", "Murattal", "https://server12.mp3quran.net/maher/"),
    QariItem("ghamdi", "Saad Al-Ghamdi", "Murattal", "https://server7.mp3quran.net/ghamdi/"),
    QariItem("dosari", "Yasser Al-Dosari", "Murattal", "https://server11.mp3quran.net/yasser/"),
    QariItem("shatri", "Abu Bakr Al-Shatri", "Murattal", "https://server11.mp3quran.net/shatri/"),
    QariItem("husary", "Mahmoud Khalil Al-Husary", "Murattal", "https://server13.mp3quran.net/husr/"),
    QariItem("minshawi", "Mohamed Al-Minshawi", "Murattal", "https://server10.mp3quran.net/minsh/"),
    QariItem("ajmi", "Ahmed Al-Ajmi", "Murattal", "https://server10.mp3quran.net/ajm/"),
    QariItem("qatami", "Nasser Al-Qatami", "Murattal", "https://server6.mp3quran.net/qtm/"),
    QariItem("rifai", "Hani Ar-Rifai", "Murattal", "https://server8.mp3quran.net/rifai/"),
    QariItem("juhany", "Abdullah Al-Juhany", "Murattal", "https://server13.mp3quran.net/jhn/"),
    QariItem("baleela", "Bandar Baleela", "Murattal", "https://server6.mp3quran.net/balila/"),
    QariItem("ayyub", "Muhammad Ayyub", "Murattal", "https://server8.mp3quran.net/ayyub/")
)

class MurottalViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(MurottalUiState())
    val uiState: StateFlow<MurottalUiState> = _uiState.asStateFlow()

    init {
        loadSurahs()
    }

    private fun loadSurahs() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val res = RetrofitClient.apiService.getSurahList()
                if (res.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        surahs = res.data,
                        filteredSurahs = res.data,
                        selectedSurah = res.data.firstOrNull()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat daftar audio murottal."
                )
            }
        }
    }

    fun openQariPicker(open: Boolean) {
        _uiState.value = _uiState.value.copy(
            isQariPickerOpen = open,
            qariSearchQuery = "",
            filteredQaris = QARI_LIST
        )
    }

    fun onQariSearchChange(query: String) {
        val filtered = if (query.trim().isEmpty()) {
            QARI_LIST
        } else {
            QARI_LIST.filter { it.name.contains(query, ignoreCase = true) }
        }
        _uiState.value = _uiState.value.copy(
            qariSearchQuery = query,
            filteredQaris = filtered
        )
    }

    fun selectQari(qari: QariItem) {
        _uiState.value = _uiState.value.copy(
            selectedQari = qari,
            isQariPickerOpen = false
        )
    }

    fun selectSurah(surah: SurahItem) {
        _uiState.value = _uiState.value.copy(selectedSurah = surah)
    }

    fun selectNextSurah(): SurahItem? {
        val current = _uiState.value.selectedSurah ?: return null
        val next = _uiState.value.surahs.find { it.nomor == current.nomor + 1 }
        if (next != null) {
            _uiState.value = _uiState.value.copy(selectedSurah = next)
        }
        return next
    }

    fun selectPrevSurah(): SurahItem? {
        val current = _uiState.value.selectedSurah ?: return null
        val prev = _uiState.value.surahs.find { it.nomor == current.nomor - 1 }
        if (prev != null) {
            _uiState.value = _uiState.value.copy(selectedSurah = prev)
        }
        return prev
    }

    fun onSurahSearchChange(query: String) {
        val filtered = if (query.trim().isEmpty()) {
            _uiState.value.surahs
        } else {
            _uiState.value.surahs.filter {
                it.namaLatin.contains(query, ignoreCase = true) ||
                it.nomor.toString() == query.trim() ||
                it.arti.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(
            surahSearchQuery = query,
            filteredSurahs = filtered
        )
    }

    fun getAudioUrl(surah: SurahItem, qari: QariItem): String {
        return if (qari.server.contains("quranicaudio.com")) {
            "${qari.server}${surah.nomor}.mp3"
        } else {
            val formattedNo = String.format(Locale.US, "%03d", surah.nomor)
            "${qari.server}${formattedNo}.mp3"
        }
    }
}
