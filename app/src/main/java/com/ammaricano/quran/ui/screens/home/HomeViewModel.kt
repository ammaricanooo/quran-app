package com.ammaricano.quran.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.SurahItem
import com.ammaricano.quran.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val surahList: List<SurahItem> = emptyList(),
    val filteredList: List<SurahItem> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "Semua", // "Semua", "Mekah", "Madinah"
    val lastRead: Triple<Int, String, Int>? = null,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            lastRead = localRepository.getLastRead()
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getSurahList()
                if (response.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        surahList = response.data,
                        filteredList = response.data
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
                    errorMessage = "Gagal memuat data. Periksa koneksi internet Anda."
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val currentList = _uiState.value.surahList
        val filter = _uiState.value.selectedFilter

        val filtered = currentList.filter { item ->
            val matchQuery = item.namaLatin.contains(query, ignoreCase = true) ||
                    item.arti.contains(query, ignoreCase = true) ||
                    item.nomor.toString() == query.trim()

            val matchFilter = when (filter) {
                "Mekah" -> item.tempatTurun.equals("Mekah", ignoreCase = true)
                "Madinah" -> item.tempatTurun.equals("Madinah", ignoreCase = true)
                else -> true
            }

            matchQuery && matchFilter
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredList = filtered
        )
    }

    fun onFilterSelect(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        onSearchQueryChange(_uiState.value.searchQuery)
    }
}
