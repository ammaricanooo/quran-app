package com.ammaricano.quran.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.SurahItem
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.components.GlassSearchBar
import com.ammaricano.quran.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSurahClick: (Int) -> Unit
) {
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
        // App Header Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.2f))
                        .border(1.dp, PrimaryBlue.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MenuBook,
                        contentDescription = null,
                        tint = PrimaryBlue2,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Assalamu'alaikum",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Al-Qur'an Ku",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Last Read Hero Card
            state.lastRead?.let { lastRead ->
                Spacer(modifier = Modifier.height(18.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSurahClick(lastRead.first) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Bookmark,
                                    contentDescription = null,
                                    tint = PrimaryBlue2,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Terakhir Dibaca",
                                    fontSize = 12.sp,
                                    color = PrimaryBlue2,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lastRead.second,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Ayat No. ${lastRead.third}",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(PrimaryBlue, PrimaryBlue2)
                                    )
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Lanjutkan",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            GlassSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Animated Sliding Filter Tabs
            val filters = listOf("Semua", "Mekah", "Madinah")
            AnimatedSlidingFilterTabs(
                filters = filters,
                selectedFilter = state.selectedFilter,
                onFilterSelected = { viewModel.onFilterSelect(it) }
            )
        }

        // Surah List
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryBlue2,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Memuat daftar surah...",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = TextMuted,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadData() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Coba Lagi")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.filteredList, key = { it.nomor }) { surah ->
                            SurahCardItem(
                                surah = surah,
                                onClick = { onSurahClick(surah.nomor) }
                            )
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

@Composable
fun AnimatedSlidingFilterTabs(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val selectedIndex = filters.indexOf(selectedFilter).coerceAtLeast(0)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceGlass)
            .border(1.dp, BorderGlass, RoundedCornerShape(16.dp))
            .padding(4.dp)
    ) {
        val density = LocalDensity.current
        var tabWidths by remember { mutableStateOf(List(filters.size) { 0.dp }) }

        // Sliding background indicator
        val animatedIndicatorOffset by animateFloatAsState(
            targetValue = selectedIndex.toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "tab_slide"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            filters.forEachIndexed { index, filter ->
                val isSelected = filter == selectedFilter

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) TextPrimary else TextMuted,
                    animationSpec = tween(200),
                    label = "tab_text_color"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(
                                    listOf(PrimaryBlue, SecondaryPurple)
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(Color.Transparent, Color.Transparent)
                                )
                            }
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onFilterSelected(filter)
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun SurahCardItem(
    surah: SurahItem,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Surah Number Diamond/Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryBlue.copy(alpha = 0.25f), SecondaryPurple.copy(alpha = 0.15f))
                        )
                    )
                    .border(
                        1.dp,
                        PrimaryBlue.copy(alpha = 0.35f),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = surah.nomor.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue2
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Surah Name (Latin & Meaning)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.namaLatin,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${surah.tempatTurun.uppercase()} • ${surah.jumlahAyat} AYAT",
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Surah Name (Arabic)
            Text(
                text = surah.nama,
                fontFamily = AyatFontFamily,
                fontSize = 20.sp,
                color = PrimaryBlue2
            )
        }
    }
}
