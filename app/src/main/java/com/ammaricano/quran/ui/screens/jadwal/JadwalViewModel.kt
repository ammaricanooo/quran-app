package com.ammaricano.quran.ui.screens.jadwal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.model.ShalatData
import com.ammaricano.quran.data.model.ShalatJadwalItem
import com.ammaricano.quran.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class JadwalUiState(
    val isLoading: Boolean = true,
    val selectedCity: CityOption = CityOption("KOTA JAKARTA", "DKI JAKARTA"),
    val shalatData: ShalatData? = null,
    val todayJadwal: ShalatJadwalItem? = null,
    val errorMessage: String? = null
)

data class CityOption(val kota: String, val provinsi: String)

class JadwalViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(JadwalUiState())
    val uiState: StateFlow<JadwalUiState> = _uiState.asStateFlow()

    val availableCities = listOf(
        CityOption("KOTA JAKARTA", "DKI JAKARTA"),
        CityOption("KOTA SURABAYA", "JAWA TIMUR"),
        CityOption("KOTA BANDUNG", "JAWA BARAT"),
        CityOption("KOTA MEDAN", "SUMATERA UTARA"),
        CityOption("KOTA SEMARANG", "JAWA TENGAH"),
        CityOption("KOTA MAKASSAR", "SULAWESI SELATAN"),
        CityOption("KOTA YOGYAKARTA", "D.I. YOGYAKARTA"),
        CityOption("KOTA PALEMBANG", "SUMATERA SELATAN"),
        CityOption("KOTA BEKASI", "JAWA BARAT"),
        CityOption("KOTA DEPOK", "JAWA BARAT"),
        CityOption("KOTA TANGERANG", "BANTEN")
    )

    init {
        loadJadwal(_uiState.value.selectedCity)
    }

    fun selectCity(city: CityOption) {
        _uiState.value = _uiState.value.copy(selectedCity = city)
        loadJadwal(city)
    }

    fun loadJadwal(city: CityOption) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val cal = Calendar.getInstance()
                val month = cal.get(Calendar.MONTH) + 1
                val year = cal.get(Calendar.YEAR)
                val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

                val body = mapOf(
                    "provinsi" to city.provinsi,
                    "kabkota" to city.kota,
                    "bulan" to month,
                    "tahun" to year
                )

                val res = RetrofitClient.apiService.getJadwalSholat(body)
                if (res.code == 200) {
                    val todayItem = res.data.jadwal.find { it.tanggal == dayOfMonth }
                        ?: res.data.jadwal.firstOrNull()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        shalatData = res.data,
                        todayJadwal = todayItem
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = res.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat jadwal sholat. Periksa internet Anda."
                )
            }
        }
    }
}
