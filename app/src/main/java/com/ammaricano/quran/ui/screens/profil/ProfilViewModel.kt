package com.ammaricano.quran.ui.screens.profil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.remote.FirebaseHelper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfilUiState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = true,
    val activeTab: Int = 0, // 0 = Ringkasan, 1 = Bookmark, 2 = Hafalan
    val lastRead: Map<String, Any?>? = null,
    val bookmarks: List<Map<String, Any?>> = emptyList(),
    val memorizedAsmaul: List<Int> = emptyList(),
    val bookmarkFilter: String = "all",
    val isEditingName: Boolean = false,
    val editNameValue: String = ""
)

class ProfilViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProfilUiState())
    val uiState: StateFlow<ProfilUiState> = _uiState.asStateFlow()

    init {
        observeAuth()
    }

    private fun observeAuth() {
        viewModelScope.launch {
            FirebaseHelper.observeAuthState().collect { firebaseUser ->
                _uiState.value = _uiState.value.copy(
                    user = firebaseUser,
                    isLoading = false
                )
                if (firebaseUser != null) {
                    observeUserData(firebaseUser.uid)
                } else {
                    _uiState.value = _uiState.value.copy(
                        lastRead = null,
                        bookmarks = emptyList(),
                        memorizedAsmaul = emptyList()
                    )
                }
            }
        }
    }

    private fun observeUserData(uid: String) {
        viewModelScope.launch {
            FirebaseHelper.observeUserData(uid).collect { data ->
                if (data != null) {
                    @Suppress("UNCHECKED_CAST")
                    _uiState.value = _uiState.value.copy(
                        lastRead = data["lastRead"] as? Map<String, Any?>,
                        bookmarks = (data["bookmarks"] as? List<Map<String, Any?>>) ?: emptyList(),
                        memorizedAsmaul = (data["asmaulHusnaMemorized"] as? List<*>)
                            ?.mapNotNull { (it as? Number)?.toInt() } ?: emptyList()
                    )
                }
            }
        }
    }

    fun setActiveTab(tab: Int) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun setBookmarkFilter(filter: String) {
        _uiState.value = _uiState.value.copy(bookmarkFilter = filter)
    }

    fun startEditName() {
        val currentName = _uiState.value.user?.displayName ?: ""
        _uiState.value = _uiState.value.copy(isEditingName = true, editNameValue = currentName)
    }

    fun cancelEditName() {
        _uiState.value = _uiState.value.copy(isEditingName = false)
    }

    fun onEditNameChange(value: String) {
        _uiState.value = _uiState.value.copy(editNameValue = value)
    }

    fun saveDisplayName() {
        val newName = _uiState.value.editNameValue.trim()
        if (newName.isBlank()) return

        viewModelScope.launch {
            try {
                FirebaseHelper.updateDisplayName(newName)
                _uiState.value = _uiState.value.copy(isEditingName = false)
            } catch (_: Exception) { }
        }
    }

    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            try {
                val bookmark = _uiState.value.bookmarks.find {
                    (it["id"] as? String) == bookmarkId
                } ?: return@launch
                FirebaseHelper.toggleBookmark(bookmark)
            } catch (_: Exception) { }
        }
    }

    fun signOut() {
        FirebaseHelper.signOut()
    }
}
