package com.ammaricano.quran.ui.screens.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ammaricano.quran.data.local.LocalRepository
import com.ammaricano.quran.data.model.BookmarkModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BookmarkUiState(
    val bookmarks: List<BookmarkModel> = emptyList()
)

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    private val localRepository = LocalRepository(application)
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    init {
        loadBookmarks()
    }

    fun loadBookmarks() {
        _uiState.value = BookmarkUiState(bookmarks = localRepository.getBookmarks())
    }

    fun removeBookmark(item: BookmarkModel) {
        localRepository.toggleBookmark(item)
        loadBookmarks()
    }
}
