package com.ammaricano.quran.ui.screens.doa

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.model.DoaItem
import com.ammaricano.quran.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DoaUiState(
    val isLoading: Boolean = true,
    val doaList: List<DoaItem> = emptyList(),
    val filteredList: List<DoaItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)

class DoaViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(DoaUiState())
    val uiState: StateFlow<DoaUiState> = _uiState.asStateFlow()

    init {
        loadDoa()
    }

    fun loadDoa() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val list = RetrofitClient.apiService.getDoaList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    doaList = list,
                    filteredList = list
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat kumpulan doa. Periksa koneksi internet."
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        val filtered = _uiState.value.doaList.filter {
            it.nama.contains(query, ignoreCase = true) ||
            it.idn.contains(query, ignoreCase = true)
        }
        _uiState.value = _uiState.value.copy(searchQuery = query, filteredList = filtered)
    }
}
