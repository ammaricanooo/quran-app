package com.ammaricano.quran.ui.screens.asmaulhusna

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.AsmaulHusnaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AsmaulHusnaUiState(
    val list: List<AsmaulHusnaItem> = emptyList(),
    val filteredList: List<AsmaulHusnaItem> = emptyList(),
    val searchQuery: String = ""
)

class AsmaulHusnaViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(AsmaulHusnaUiState())
    val uiState: StateFlow<AsmaulHusnaUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val data = localRepository.getAsmaulHusna()
        _uiState.value = AsmaulHusnaUiState(list = data, filteredList = data)
    }

    fun onSearchQueryChange(query: String) {
        val filtered = _uiState.value.list.filter {
            it.latin.contains(query, ignoreCase = true) ||
            it.indo.contains(query, ignoreCase = true) ||
            it.id.toString() == query.trim()
        }
        _uiState.value = _uiState.value.copy(searchQuery = query, filteredList = filtered)
    }
}
