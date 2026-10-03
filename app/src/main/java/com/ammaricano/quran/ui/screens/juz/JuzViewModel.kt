package com.ammaricano.quran.ui.screens.juz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.JuzItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class JuzUiState(
    val list: List<JuzItem> = emptyList()
)

class JuzViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(JuzUiState())
    val uiState: StateFlow<JuzUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = JuzUiState(list = localRepository.getJuzList())
    }
}
