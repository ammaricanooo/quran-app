package com.ammaricano.quran.ui.screens.tahlil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.TahlilItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TahlilUiState(
    val items: List<TahlilItem> = emptyList()
)

class TahlilViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(TahlilUiState())
    val uiState: StateFlow<TahlilUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = TahlilUiState(items = localRepository.getTahlilData())
    }
}
