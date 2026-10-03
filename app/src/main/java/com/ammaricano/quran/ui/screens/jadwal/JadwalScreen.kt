package com.ammaricano.quran.ui.screens.jadwal

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.CityOption
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalScreen(viewModel: JadwalViewModel) {
    val state by viewModel.uiState.collectAsState()
    val todayDateFormatted = remember {
        SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgPrimary, BgPrimary2, BgPrimary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header Bar matching Web
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Jadwal ",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Shalat",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = PrimaryBlue2
                        )
                    }
                    Text(
                        text = todayDateFormatted,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.50f)
                    )
                }

                // City Selector Button (Web Style)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.openCityPicker(true) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = PrimaryBlue2,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = state.selectedCity.kota.replace("Kota ", "").replace("Kabupaten ", "Kab. "),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 120.dp)
                        )
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.60f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Main Scrollable Area
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Dynamic Desert Sky Banner (Web Style)
                item {
                    DesertSkyBanner(
                        timeOfDay = state.timeOfDay,
                        cityName = state.selectedCity.label,
                        nextPrayerName = state.nextPrayerName,
                        nextPrayerTime = state.nextPrayerTime,
                        countdown = state.nextPrayerCountdown,
                        todayDate = todayDateFormatted
                    )
                }

                // 2. Section Title
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WAKTU SHALAT HARI INI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = Color.White.copy(alpha = 0.50f)
                        )
                        Text(
                            text = "WIB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = PrimaryBlue2
                        )
                    }
                }

                // 3. Prayer Cards (8 Items)
                val j = state.todayJadwal
                val prayerList = listOf(
                    PrayerItemUi("Imsak", j.imsak, Icons.Rounded.Schedule),
                    PrayerItemUi("Subuh", j.subuh, Icons.Rounded.Brightness5),
                    PrayerItemUi("Terbit", j.terbit, Icons.Rounded.WbSunny),
                    PrayerItemUi("Dhuha", j.dhuha, Icons.Rounded.WbSunny),
                    PrayerItemUi("Dzuhur", j.dzuhur, Icons.Rounded.Brightness5),
                    PrayerItemUi("Ashar", j.ashar, Icons.Rounded.Brightness6),
                    PrayerItemUi("Maghrib", j.maghrib, Icons.Rounded.Brightness6),
                    PrayerItemUi("Isya", j.isya, Icons.Rounded.Bedtime)
                )

                items(prayerList) { prayer ->
                    val isNext = prayer.name.equals(state.nextPrayerName, ignoreCase = true)
                    PrayerCardItem(prayer = prayer, isHighlighted = isNext)
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // City Search Bottom Sheet (514 Cities)
        if (state.isCityPickerOpen) {
            CitySearchBottomSheet(
                query = state.citySearchQuery,
                onQueryChange = { viewModel.onCitySearchQueryChange(it) },
                filteredCities = state.filteredCities,
                currentCity = state.selectedCity,
                onSelectCity = { viewModel.selectCity(it) },
                onDismiss = { viewModel.openCityPicker(false) }
            )
        }
    }
}

// ─── Desert Sky Banner ────────────────────────────────────────────────────────
@Composable
fun DesertSkyBanner(
    timeOfDay: String,
    cityName: String,
    nextPrayerName: String,
    nextPrayerTime: String,
    countdown: String,
    todayDate: String
) {
    val skyGradient = when (timeOfDay) {
        "subuh" -> listOf(Color(0xFF101E42), Color(0xFF243763), Color(0xFFD97757))
        "pagi" -> listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFFFDBA74))
        "siang" -> listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF38BDF8))
        "sore" -> listOf(Color(0xFF4C0519), Color(0xFFC2410C), Color(0xFFF97316))
        else -> listOf(Color(0xFF030712), Color(0xFF091124), Color(0xFF111F3D))
    }

    val sunMoonColor = when (timeOfDay) {
        "malam" -> Color(0xFFE2E8F0)
        "subuh" -> Color(0xFFFEF3C7)
        else -> Color(0xFFFEF08A)
    }

    val timeOfDayLabel = when (timeOfDay) {
        "subuh" -> "Waktu Subuh"
        "pagi" -> "Pagi Hari"
        "siang" -> "Siang Hari"
        "sore" -> "Sore / Senja"
        else -> "Malam Hari"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(skyGradient))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top row: Sun/Moon orb + badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Sun / Moon Orb with ambient ring
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(sunMoonColor)
                            .border(3.dp, sunMoonColor.copy(alpha = 0.35f), CircleShape)
                    )

                    // Time of day badge
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = timeOfDayLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Location badge
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.35f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            tint = PrimaryBlue2,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = cityName.split(",").firstOrNull() ?: cityName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Middle: Next Prayer Countdown
            Text(
                text = "MENILIK WAKTU $nextPrayerName ($nextPrayerTime WIB)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Countdown Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.50f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue2)
                        )
                        Text(
                            text = countdown,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    }
                }

                // Date Label
                Text(
                    text = todayDate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.80f)
                )
            }
        }
    }
}

// ─── Prayer Card Item ─────────────────────────────────────────────────────────
data class PrayerItemUi(
    val name: String,
    val time: String,
    val icon: ImageVector
)

@Composable
fun PrayerCardItem(
    prayer: PrayerItemUi,
    isHighlighted: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isHighlighted) {
                    Brush.linearGradient(listOf(PrimaryBlue, PrimaryBlue2))
                } else {
                    Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.05f), Color.White.copy(alpha = 0.03f))
                    )
                }
            )
            .border(
                1.dp,
                if (isHighlighted) PrimaryBlue2.copy(alpha = 0.60f) else Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Indicator stripe for highlighted
                if (isHighlighted) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isHighlighted) Color.White.copy(alpha = 0.20f)
                            else Color.White.copy(alpha = 0.06f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = prayer.icon,
                        contentDescription = null,
                        tint = if (isHighlighted) Color.White else PrimaryBlue2,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = prayer.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (isHighlighted) {
                        Text(
                            text = "Dinantikan",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Text(
                text = prayer.time.ifEmpty { "--:--" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
        }
    }
}

// ─── City Search Bottom Sheet (514 Cities) ───────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySearchBottomSheet(
    query: String,
    onQueryChange: (String) -> Unit,
    filteredCities: List<CityOption>,
    currentCity: CityOption,
    onSelectCity: (CityOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BgPrimary2,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.30f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PILIH KOTA / KABUPATEN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    color = Color.White
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Tutup",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Cari kota atau provinsi...",
                        color = Color.White.copy(alpha = 0.40f),
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.50f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear",
                                tint = Color.White.copy(alpha = 0.60f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.05f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                    focusedBorderColor = PrimaryBlue2,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.10f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cities List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredCities) { city ->
                    val isSelected = city.kota.equals(currentCity.kota, ignoreCase = true)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) PrimaryBlue.copy(alpha = 0.20f)
                                else Color.White.copy(alpha = 0.03f)
                            )
                            .border(
                                1.dp,
                                if (isSelected) PrimaryBlue.copy(alpha = 0.35f)
                                else Color.White.copy(alpha = 0.05f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectCity(city) }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) PrimaryBlue2 else Color.White.copy(alpha = 0.40f),
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = city.kota,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = city.provinsi,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.45f)
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = PrimaryBlue2,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
