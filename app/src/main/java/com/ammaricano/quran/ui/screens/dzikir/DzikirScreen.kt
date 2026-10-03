package com.ammaricano.quran.ui.screens.dzikir

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbSunny
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.DzikirItem
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.EmeraldGreen
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.SecondaryPurple
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@Composable
fun DzikirScreen(viewModel: DzikirViewModel) {
    val state by viewModel.uiState.collectAsState()

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
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Text(
                text = "Dzikir Harian",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Dzikir Pagi & Petang Sesuai Sunnah",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Switch Tabs (Pagi / Petang)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                val isPagi = state.selectedTab == "pagi"

                // Pagi tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isPagi) PrimaryBlue else Color.Transparent)
                        .clickable { viewModel.selectTab("pagi") }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.WbSunny,
                            contentDescription = null,
                            tint = if (isPagi) Color.White else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Dzikir Pagi",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPagi) Color.White else TextMuted
                        )
                    }
                }

                // Petang tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isPagi) SecondaryPurple else Color.Transparent)
                        .clickable { viewModel.selectTab("petang") }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.NightsStay,
                            contentDescription = null,
                            tint = if (!isPagi) Color.White else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Dzikir Petang",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isPagi) Color.White else TextMuted
                        )
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            items(state.filteredItems, key = { it.id }) { item ->
                val maxRepeat = item.ulang.replace("x", "").trim().toIntOrNull() ?: 1
                val currentCount = state.counters[item.id] ?: 0

                DzikirCardItem(
                    item = item,
                    currentCount = currentCount,
                    maxCount = maxRepeat,
                    onIncrement = { viewModel.incrementCounter(item.id, maxRepeat) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun DzikirCardItem(
    item: DzikirItem,
    currentCount: Int,
    maxCount: Int,
    onIncrement: () -> Unit
) {
    val isCompleted = currentCount >= maxCount

    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Arabic
        Text(
            text = item.arab,
            fontFamily = AyatFontFamily,
            fontSize = 22.sp,
            lineHeight = 42.sp,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Indonesian translation
        Text(
            text = item.indo,
            fontSize = 12.sp,
            color = TextSubtitle,
            lineHeight = 18.sp
        )

        item.faedah?.let { faedah ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Keutamaan: $faedah",
                fontSize = 11.sp,
                color = PrimaryBlue2
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Repeat counter button (Tasbih Digital)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dibaca ${item.ulang}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isCompleted) EmeraldGreen.copy(alpha = 0.25f)
                        else PrimaryBlue.copy(alpha = 0.2f)
                    )
                    .border(
                        1.dp,
                        if (isCompleted) EmeraldGreen else PrimaryBlue2,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onIncrement() }
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Selesai",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    } else {
                        Text(
                            text = "$currentCount / $maxCount",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue2
                        )
                    }
                }
            }
        }
    }
}
