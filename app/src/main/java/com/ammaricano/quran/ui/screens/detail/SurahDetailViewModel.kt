package com.ammaricano.quran.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.AyatItem
import com.ammaricano.quran.data.model.BookmarkModel
import com.ammaricano.quran.data.model.SurahDetailData
import com.ammaricano.quran.data.model.TafsirItem
import com.ammaricano.quran.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SurahDetailUiState(
    val isLoading: Boolean = true,
    val selectedTab: String = "ayat", // "ayat" or "tafsir"
    val surahDetail: SurahDetailData? = null,
    val tafsirList: List<TafsirItem> = emptyList(),
    val errorMessage: String? = null,
    val arabicFontSize: Float = 24f,
    val bookmarkedAyatNumbers: Set<Int> = emptySet()
)

class SurahDetailViewModel(
    application: Application,
    private val surahNomor: Int
) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(SurahDetailUiState())
    val uiState: StateFlow<SurahDetailUiState> = _uiState.asStateFlow()

    init {
        val initialSize = localRepository.getArabicFontSize()
        _uiState.value = _uiState.value.copy(arabicFontSize = initialSize)
        loadDetail()
        refreshBookmarks()
    }

    fun selectTab(tab: String) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        if (tab == "tafsir" && _uiState.value.tafsirList.isEmpty()) {
            loadTafsir()
        }
    }

    fun setArabicFontSize(size: Float) {
        localRepository.setArabicFontSize(size)
        _uiState.value = _uiState.value.copy(arabicFontSize = size)
    }

    private fun refreshBookmarks() {
        val bookmarked = localRepository.getBookmarks()
            .filter { it.surahNo == surahNomor }
            .map { it.ayatNo }
            .toSet()
        _uiState.value = _uiState.value.copy(bookmarkedAyatNumbers = bookmarked)
    }

    fun loadDetail() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getSurahDetail(surahNomor)
                if (response.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        surahDetail = response.data
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat ayat surah. Silakan coba lagi."
                )
            }
        }
    }

    fun loadTafsir() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getTafsir(surahNomor)
                if (response.code == 200) {
                    _uiState.value = _uiState.value.copy(tafsirList = response.data.tafsir)
                }
            } catch (e: Exception) {
                // Ignore silent failure
            }
        }
    }

    fun toggleBookmarkAyat(ayat: AyatItem) {
        val detail = _uiState.value.surahDetail ?: return
        localRepository.saveLastRead(detail.nomor, detail.namaLatin, ayat.nomorAyat)
        val bookmark = BookmarkModel(
            surahNo = detail.nomor,
            surahName = detail.namaLatin,
            ayatNo = ayat.nomorAyat,
            arabSnippet = ayat.teksArab,
            indoSnippet = ayat.teksIndonesia
        )
        localRepository.toggleBookmark(bookmark)
        refreshBookmarks()
    }
}
