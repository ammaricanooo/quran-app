package com.ammaricano.quran.ui.screens.hadits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.HaditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HaditsUiState(
    val list: List<HaditsItem> = emptyList(),
    val filteredList: List<HaditsItem> = emptyList(),
    val searchQuery: String = ""
)

class HaditsViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(HaditsUiState())
    val uiState: StateFlow<HaditsUiState> = _uiState.asStateFlow()

    init {
        val data = localRepository.getHaditsArbain()
        _uiState.value = HaditsUiState(list = data, filteredList = data)
    }

    fun onSearchQueryChange(query: String) {
        val filtered = _uiState.value.list.filter {
            it.judul.contains(query, ignoreCase = true) ||
            it.indo.contains(query, ignoreCase = true) ||
            it.no.toString() == query.trim()
        }
        _uiState.value = _uiState.value.copy(searchQuery = query, filteredList = filtered)
    }
}
