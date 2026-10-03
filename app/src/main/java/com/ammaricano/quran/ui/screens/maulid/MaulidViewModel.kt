package com.ammaricano.quran.ui.screens.maulid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class MaulidReadingItem(
    val id: Int,
    val order: Int,
    val arabic: String,
    val transliteration: String,
    val translate: String
)

data class MaulidSubcategory(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val total: Int,
    val readings: List<MaulidReadingItem>
)

data class MaulidKitab(
    val id: Int,
    val name: String,
    val slug: String,
    val total: Int,
    val subcategories: List<MaulidSubcategory>
)

data class MaulidUiState(
    val kitabs: List<MaulidKitab> = emptyList(),
    val selectedKitab: MaulidKitab? = null,
    val selectedSubcategory: MaulidSubcategory? = null,
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

class MaulidViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(MaulidUiState())
    val uiState: StateFlow<MaulidUiState> = _uiState.asStateFlow()

    init {
        loadMaulidData()
    }

    private fun loadMaulidData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val kitabs = withContext(Dispatchers.IO) {
                try {
                    val jsonStr = getApplication<Application>().assets
                        .open("maulid_nabi.json")
                        .bufferedReader()
                        .use { it.readText() }

                    val root = JSONObject(jsonStr)
                    val catArr = root.optJSONArray("categories") ?: return@withContext emptyList()
                    val result = mutableListOf<MaulidKitab>()

                    for (i in 0 until catArr.length()) {
                        val catObj = catArr.getJSONObject(i)
                        val id = catObj.optInt("id")
                        val name = catObj.optString("name")
                        val slug = catObj.optString("slug")
                        val total = catObj.optInt("total")

                        val subArr = catObj.optJSONArray("subcategories")
                        val subList = mutableListOf<MaulidSubcategory>()

                        if (subArr != null) {
                            for (j in 0 until subArr.length()) {
                                val subObj = subArr.getJSONObject(j)
                                val subId = subObj.optInt("id")
                                val subName = subObj.optString("name")
                                val subSlug = subObj.optString("slug")
                                val subDesc = subObj.optString("description")
                                val subTotal = subObj.optInt("total")

                                val readArr = subObj.optJSONArray("readings")
                                val readList = mutableListOf<MaulidReadingItem>()
                                if (readArr != null) {
                                    for (k in 0 until readArr.length()) {
                                        val readObj = readArr.getJSONObject(k)
                                        readList.add(
                                            MaulidReadingItem(
                                                id = readObj.optInt("id"),
                                                order = readObj.optInt("order"),
                                                arabic = readObj.optString("arabic"),
                                                transliteration = readObj.optString("transliteration"),
                                                translate = readObj.optString("translate")
                                            )
                                        )
                                    }
                                }

                                subList.add(
                                    MaulidSubcategory(
                                        id = subId,
                                        name = subName,
                                        slug = subSlug,
                                        description = subDesc,
                                        total = subTotal,
                                        readings = readList
                                    )
                                )
                            }
                        }

                        result.add(
                            MaulidKitab(
                                id = id,
                                name = name,
                                slug = slug,
                                total = total,
                                subcategories = subList
                            )
                        )
                    }
                    result
                } catch (e: Exception) {
                    emptyList()
                }
            }

            _uiState.value = _uiState.value.copy(
                kitabs = kitabs,
                isLoading = false
            )
        }
    }

    fun selectKitab(kitab: MaulidKitab?) {
        _uiState.value = _uiState.value.copy(
            selectedKitab = kitab,
            selectedSubcategory = null
        )
    }

    fun selectSubcategory(subcategory: MaulidSubcategory?) {
        _uiState.value = _uiState.value.copy(selectedSubcategory = subcategory)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
