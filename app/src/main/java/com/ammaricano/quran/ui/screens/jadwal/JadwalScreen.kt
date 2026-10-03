package com.ammaricano.quran.ui.screens.jadwal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Brightness5
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.SecondaryPurple
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JadwalScreen(viewModel: JadwalViewModel) {
    val state by viewModel.uiState.collectAsState()
    val todayDateFormatted = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID")).format(Date())

    Column(
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
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {
            Text(
                text = "Jadwal Sholat",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = todayDateFormatted,
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // City Horizontal Selector
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.availableCities) { city ->
                    val isSelected = state.selectedCity.kota == city.kota
                    val cityName = city.kota.replace("KOTA ", "").replace("KAB. ", "")

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryBlue else Color.White.copy(alpha = 0.05f))
                            .border(
                                1.dp,
                                if (isSelected) PrimaryBlue else Color.White.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .clickable { viewModel.selectCity(city) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = cityName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextMuted
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = PrimaryBlue2)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Memuat jadwal sholat...", color = TextMuted, fontSize = 13.sp)
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.errorMessage ?: "", color = TextMuted, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.loadJadwal(state.selectedCity) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Rounded.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Coba Lagi")
                        }
                    }
                }

                state.todayJadwal != null -> {
                    val j = state.todayJadwal!!

                    val prayerTimes = listOf(
                        PrayerItem("Imsak", j.imsak, Icons.Rounded.Schedule, Color(0xFF94A3B8)),
                        PrayerItem("Subuh", j.subuh, Icons.Rounded.Brightness5, PrimaryBlue2),
                        PrayerItem("Terbit", j.terbit, Icons.Rounded.WbSunny, Color(0xFFFBBF24)),
                        PrayerItem("Dhuha", j.dhuha, Icons.Rounded.WbSunny, Color(0xFFF59E0B)),
                        PrayerItem("Dzuhur", j.dzuhur, Icons.Rounded.Brightness5, Color(0xFF38BDF8)),
                        PrayerItem("Ashar", j.ashar, Icons.Rounded.Brightness6, Color(0xFFFB923C)),
                        PrayerItem("Maghrib", j.maghrib, Icons.Rounded.Brightness6, Color(0xFFF43F5E)),
                        PrayerItem("Isya", j.isya, Icons.Rounded.Bedtime, Color(0xFFA855F7))
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Hero card
                        item {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Wilayah", fontSize = 11.sp, color = TextMuted)
                                        Text(
                                            state.selectedCity.kota,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            state.selectedCity.provinsi,
                                            fontSize = 12.sp,
                                            color = PrimaryBlue2
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlue.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Rounded.Schedule,
                                            contentDescription = null,
                                            tint = PrimaryBlue2,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Prayer Cards
                        items(prayerTimes) { prayer ->
                            PrayerCardRow(prayer = prayer)
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

data class PrayerItem(
    val name: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun PrayerCardRow(prayer: PrayerItem) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(prayer.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = prayer.icon,
                        contentDescription = null,
                        tint = prayer.color,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = prayer.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Text(
                text = "${prayer.time} WIB",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = prayer.color
            )
        }
    }
}
