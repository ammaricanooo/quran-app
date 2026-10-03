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

data class QariOption(val id: String, val name: String)

data class MurottalUiState(
    val isLoading: Boolean = true,
    val surahs: List<SurahItem> = emptyList(),
    val selectedSurah: SurahItem? = null,
    val selectedQari: QariOption = QariOption("05", "Misyari Rasyid Al-'Afasy"),
    val searchQuery: String = "",
    val errorMessage: String? = null
)

class MurottalViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(MurottalUiState())
    val uiState: StateFlow<MurottalUiState> = _uiState.asStateFlow()

    val qariList = listOf(
        QariOption("05", "Misyari Rasyid Al-'Afasy"),
        QariOption("01", "Abdullah Al-Juhany"),
        QariOption("02", "Abdul Muhsin Al-Qasim"),
        QariOption("03", "Abdurrahman As-Sudais"),
        QariOption("04", "Ibrahim Al-Dossari")
    )

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

    fun selectQari(qari: QariOption) {
        _uiState.value = _uiState.value.copy(selectedQari = qari)
    }

    fun selectSurah(surah: SurahItem) {
        _uiState.value = _uiState.value.copy(selectedSurah = surah)
    }

    fun getAudioUrl(surah: SurahItem, qariId: String): String? {
        return surah.audioFull?.get(qariId) ?: surah.audioFull?.values?.firstOrNull()
    }
}
