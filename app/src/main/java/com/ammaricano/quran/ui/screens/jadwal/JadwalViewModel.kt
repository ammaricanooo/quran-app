package com.ammaricano.quran.ui.screens.jadwal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ammaricano.quran.data.model.CityOption
import com.ammaricano.quran.data.model.ShalatData
import com.ammaricano.quran.data.model.ShalatJadwalItem
import com.ammaricano.quran.data.remote.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class JadwalUiState(
    val isLoading: Boolean = false,
    val selectedCity: CityOption = CityOption(kota = "Kota Jakarta", provinsi = "DKI Jakarta", label = "Jakarta, DKI Jakarta"),
    val shalatData: ShalatData? = null,
    val todayJadwal: ShalatJadwalItem = ShalatJadwalItem(),
    val nextPrayerName: String = "Dzuhur",
    val nextPrayerCountdown: String = "--:--:--",
    val nextPrayerTime: String = "11:55",
    val timeOfDay: String = "siang", // subuh, pagi, siang, sore, malam
    val allCities: List<CityOption> = emptyList(),
    val citySearchQuery: String = "",
    val filteredCities: List<CityOption> = emptyList(),
    val isCityPickerOpen: Boolean = false,
    val errorMessage: String? = null
)

class JadwalViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(JadwalUiState())
    val uiState: StateFlow<JadwalUiState> = _uiState.asStateFlow()

    init {
        loadCities()
        loadJadwal(_uiState.value.selectedCity)
        startClockTimer()
    }

    private fun loadCities() {
        viewModelScope.launch {
            val cities = withContext(Dispatchers.IO) {
                try {
                    val jsonStr = getApplication<Application>().assets
                        .open("data_kota.json")
                        .bufferedReader()
                        .use { it.readText() }

                    val type = object : TypeToken<List<CityOption>>() {}.type
                    Gson().fromJson<List<CityOption>>(jsonStr, type) ?: defaultCities()
                } catch (e: Exception) {
                    defaultCities()
                }
            }
            _uiState.value = _uiState.value.copy(
                allCities = cities,
                filteredCities = cities.take(20)
            )
        }
    }

    private fun defaultCities(): List<CityOption> = listOf(
        CityOption("Kota Jakarta", "DKI Jakarta", "Jakarta, DKI Jakarta"),
        CityOption("Kota Surabaya", "Jawa Timur", "Surabaya, Jawa Timur"),
        CityOption("Kota Bandung", "Jawa Barat", "Bandung, Jawa Barat"),
        CityOption("Kota Medan", "Sumatera Utara", "Medan, Sumatera Utara"),
        CityOption("Kota Semarang", "Jawa Tengah", "Semarang, Jawa Tengah"),
        CityOption("Kota Makassar", "Sulawesi Selatan", "Makassar, Sulawesi Selatan"),
        CityOption("Kota Yogyakarta", "D.I. Yogyakarta", "Yogyakarta, D.I. Yogyakarta")
    )

    fun openCityPicker(open: Boolean) {
        _uiState.value = _uiState.value.copy(
            isCityPickerOpen = open,
            citySearchQuery = "",
            filteredCities = _uiState.value.allCities.take(20)
        )
    }

    fun onCitySearchQueryChange(query: String) {
        val filtered = if (query.trim().isEmpty()) {
            _uiState.value.allCities.take(20)
        } else {
            _uiState.value.allCities.filter {
                it.label.contains(query, ignoreCase = true) ||
                it.kota.contains(query, ignoreCase = true) ||
                it.provinsi.contains(query, ignoreCase = true)
            }.take(30)
        }
        _uiState.value = _uiState.value.copy(
            citySearchQuery = query,
            filteredCities = filtered
        )
    }

    fun selectCity(city: CityOption) {
        _uiState.value = _uiState.value.copy(
            selectedCity = city,
            isCityPickerOpen = false
        )
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
                if (res.code == 200 && res.data != null && res.data.jadwal.isNotEmpty()) {
                    val todayItem = res.data.jadwal.find { it.tanggal == dayOfMonth }
                        ?: res.data.jadwal.first()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        shalatData = res.data,
                        todayJadwal = todayItem
                    )
                    updateCountdown(todayItem)
                } else {
                    useFallbackSchedule(dayOfMonth)
                }
            } catch (e: Exception) {
                val cal = Calendar.getInstance()
                useFallbackSchedule(cal.get(Calendar.DAY_OF_MONTH))
            }
        }
    }

    private fun useFallbackSchedule(dayOfMonth: Int) {
        val fallback = ShalatJadwalItem(
            tanggal = dayOfMonth,
            imsak = "04:15",
            subuh = "04:25",
            terbit = "05:38",
            dhuha = "06:01",
            dzuhur = "11:47",
            ashar = "14:55",
            maghrib = "17:51",
            isya = "19:00"
        )
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            todayJadwal = fallback,
            errorMessage = null
        )
        updateCountdown(fallback)
    }

    private fun startClockTimer() {
        viewModelScope.launch {
            while (isActive) {
                val current = _uiState.value.todayJadwal
                updateCountdown(current)
                delay(1000)
            }
        }
    }

    private fun updateCountdown(j: ShalatJadwalItem) {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)
        val currentSecond = now.get(Calendar.SECOND)
        val currentSecondsToday = currentHour * 3600 + currentMinute * 60 + currentSecond

        val prayerList = listOf(
            "Subuh" to j.subuh,
            "Terbit" to j.terbit,
            "Dzuhur" to j.dzuhur,
            "Ashar" to j.ashar,
            "Maghrib" to j.maghrib,
            "Isya" to j.isya
        )

        fun parseSeconds(timeStr: String): Int {
            val parts = timeStr.split(":")
            val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
            return h * 3600 + m * 60
        }

        var nextName = "Subuh"
        var nextTime = j.subuh
        var diffSeconds = 0

        for ((name, timeStr) in prayerList) {
            val s = parseSeconds(timeStr)
            if (s > currentSecondsToday) {
                nextName = name
                nextTime = timeStr
                diffSeconds = s - currentSecondsToday
                break
            }
        }

        if (diffSeconds == 0) {
            // Next is tomorrow's Subuh
            val subuhSec = parseSeconds(j.subuh)
            diffSeconds = (24 * 3600 - currentSecondsToday) + subuhSec
            nextName = "Subuh"
            nextTime = j.subuh
        }

        val h = diffSeconds / 3600
        val m = (diffSeconds % 3600) / 60
        val sec = diffSeconds % 60
        val countdownStr = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, sec)

        // Time of day determination
        val timeOfDay = when (currentHour) {
            in 4..5 -> "subuh"
            in 6..10 -> "pagi"
            in 11..14 -> "siang"
            in 15..18 -> "sore"
            else -> "malam"
        }

        _uiState.value = _uiState.value.copy(
            nextPrayerName = nextName,
            nextPrayerTime = nextTime,
            nextPrayerCountdown = countdownStr,
            timeOfDay = timeOfDay
        )
    }
}
