package com.ammaricano.quran.ui.screens.dzikir

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.DzikirItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DzikirUiState(
    val selectedTab: String = "pagi", // "pagi" or "petang"
    val allItems: List<DzikirItem> = emptyList(),
    val filteredItems: List<DzikirItem> = emptyList(),
    val counters: Map<String, Int> = emptyMap()
)

class DzikirViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(DzikirUiState())
    val uiState: StateFlow<DzikirUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val items = localRepository.getDzikirData()
        _uiState.value = DzikirUiState(
            allItems = items,
            filteredItems = items.filter { it.type == "pagi" }
        )
    }

    fun selectTab(tab: String) {
        _uiState.value = _uiState.value.copy(
            selectedTab = tab,
            filteredItems = _uiState.value.allItems.filter { it.type == tab }
        )
    }

    fun incrementCounter(id: String, maxCount: Int) {
        val current = _uiState.value.counters[id] ?: 0
        val next = if (current + 1 > maxCount) maxCount else current + 1
        _uiState.value = _uiState.value.copy(
            counters = _uiState.value.counters + (id to next)
        )
    }

    fun resetCounter(id: String) {
        _uiState.value = _uiState.value.copy(
            counters = _uiState.value.counters + (id to 0)
        )
    }
}
