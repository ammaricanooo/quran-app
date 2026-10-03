package com.ammaricano.quran.ui.screens.murottal

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.SurahItem
import com.ammaricano.quran.service.AudioPlayerManager
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MurottalScreen(
    viewModel: MurottalViewModel,
    audioPlayerManager: AudioPlayerManager
) {
    val state by viewModel.uiState.collectAsState()
    val playbackState by audioPlayerManager.playbackState.collectAsState()

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
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: Title & Qari Selector Pill (Web Style)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 42.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Murottal ",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Al-Qur'an",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = PrimaryBlue2
                        )
                    }
                    Text(
                        text = "Lantunan 30 Juz 15 Qari Terkemuka",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.50f)
                    )
                }

                // Qari Picker Trigger Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.openQariPicker(true) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = PrimaryBlue2,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = state.selectedQari.name,
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

            // Main Content Area
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Hero Player Card (Matching Web app/murottal/page.tsx)
                state.selectedSurah?.let { surah ->
                    val audioUrl = viewModel.getAudioUrl(surah, state.selectedQari)
                    val isCurrentSurahPlaying = playbackState.currentUrl == audioUrl && playbackState.isPlaying

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(28.dp))
                                .padding(22.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Top Badges
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(PrimaryBlue.copy(alpha = 0.20f))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Surah ke-${surah.nomor}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryBlue2
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.06f))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${surah.jumlahAyat} Ayat",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.60f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Arabic Typography
                                Text(
                                    text = surah.nama,
                                    fontFamily = AyatFontFamily,
                                    fontSize = 40.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 54.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Latin Name
                                Text(
                                    text = surah.namaLatin,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )

                                Text(
                                    text = surah.arti,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.50f)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Qari Attribution
                                Text(
                                    text = "Dibaca oleh ${state.selectedQari.name}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = PrimaryBlue2
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Controls: Prev, Play/Pause, Next
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Prev Surah
                                    IconButton(
                                        onClick = {
                                            val prev = viewModel.selectPrevSurah()
                                            if (prev != null) {
                                                val url = viewModel.getAudioUrl(prev, state.selectedQari)
                                                audioPlayerManager.playAudio(
                                                    url = url,
                                                    title = "Surah ${prev.namaLatin}",
                                                    subtitle = state.selectedQari.name
                                                )
                                            }
                                        },
                                        enabled = surah.nomor > 1,
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.06f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.SkipPrevious,
                                            contentDescription = "Sebelumnya",
                                            tint = if (surah.nomor > 1) Color.White else Color.White.copy(alpha = 0.20f),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    // Big Play/Pause Button (Primary Blue)
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlue)
                                            .clickable {
                                                audioPlayerManager.playAudio(
                                                    url = audioUrl,
                                                    title = "Surah ${surah.namaLatin}",
                                                    subtitle = state.selectedQari.name
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isCurrentSurahPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                            contentDescription = "Putar",
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    // Next Surah
                                    IconButton(
                                        onClick = {
                                            val next = viewModel.selectNextSurah()
                                            if (next != null) {
                                                val url = viewModel.getAudioUrl(next, state.selectedQari)
                                                audioPlayerManager.playAudio(
                                                    url = url,
                                                    title = "Surah ${next.namaLatin}",
                                                    subtitle = state.selectedQari.name
                                                )
                                            }
                                        },
                                        enabled = surah.nomor < 114,
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.06f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.SkipNext,
                                            contentDescription = "Berikutnya",
                                            tint = if (surah.nomor < 114) Color.White else Color.White.copy(alpha = 0.20f),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Search Surah Bar
                item {
                    OutlinedTextField(
                        value = state.surahSearchQuery,
                        onValueChange = { viewModel.onSurahSearchChange(it) },
                        placeholder = {
                            Text("Cari nama atau nomor surah...", color = Color.White.copy(alpha = 0.40f), fontSize = 13.sp)
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
                            if (state.surahSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSurahSearchChange("") }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "Hapus",
                                        tint = Color.White.copy(alpha = 0.60f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.05f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                            focusedBorderColor = PrimaryBlue2,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }

                // 3. Section Title
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAFTAR 114 SURAH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = Color.White.copy(alpha = 0.50f)
                        )
                        Text(
                            text = "${state.filteredSurahs.size} Surah",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue2
                        )
                    }
                }

                // 4. Surah List Items
                items(state.filteredSurahs, key = { it.nomor }) { surah ->
                    val isSelected = state.selectedSurah?.nomor == surah.nomor
                    val audioUrl = viewModel.getAudioUrl(surah, state.selectedQari)
                    val isThisPlaying = playbackState.currentUrl == audioUrl && playbackState.isPlaying

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) PrimaryBlue.copy(alpha = 0.15f)
                                else Color.White.copy(alpha = 0.04f)
                            )
                            .border(
                                1.dp,
                                if (isSelected) PrimaryBlue2.copy(alpha = 0.40f)
                                else Color.White.copy(alpha = 0.06f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                viewModel.selectSurah(surah)
                                audioPlayerManager.playAudio(
                                    url = audioUrl,
                                    title = "Surah ${surah.namaLatin}",
                                    subtitle = state.selectedQari.name
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Nomor Badge
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) PrimaryBlue
                                            else Color.White.copy(alpha = 0.06f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isThisPlaying) {
                                        Icon(
                                            imageVector = Icons.Rounded.Headphones,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Text(
                                            text = surah.nomor.toString(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.65f)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = surah.namaLatin,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${surah.tempatTurun} • ${surah.jumlahAyat} Ayat",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.45f)
                                    )
                                }
                            }

                            Text(
                                text = surah.nama,
                                fontFamily = AyatFontFamily,
                                fontSize = 20.sp,
                                color = if (isSelected) PrimaryBlue2 else Color.White.copy(alpha = 0.70f)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // Qari Picker BottomSheet (All 15 Qari)
        if (state.isQariPickerOpen) {
            QariPickerBottomSheet(
                query = state.qariSearchQuery,
                onQueryChange = { viewModel.onQariSearchChange(it) },
                qariList = state.filteredQaris,
                selectedQari = state.selectedQari,
                onSelectQari = { qari ->
                    viewModel.selectQari(qari)
                    // If playing, switch audio stream smoothly to new Qari
                    state.selectedSurah?.let { surah ->
                        if (playbackState.isPlaying) {
                            val newUrl = viewModel.getAudioUrl(surah, qari)
                            audioPlayerManager.playAudio(
                                url = newUrl,
                                title = "Surah ${surah.namaLatin}",
                                subtitle = qari.name
                            )
                        }
                    }
                },
                onDismiss = { viewModel.openQariPicker(false) }
            )
        }
    }
}

// ─── Qari Picker Bottom Sheet ────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QariPickerBottomSheet(
    query: String,
    onQueryChange: (String) -> Unit,
    qariList: List<QariItem>,
    selectedQari: QariItem,
    onSelectQari: (QariItem) -> Unit,
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
                    text = "PILIH QARI (15 QARI)",
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
                        text = "Cari nama qari...",
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

            // Qari List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(qariList) { qari ->
                    val isSelected = qari.id == selectedQari.id

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
                            .clickable {
                                onSelectQari(qari)
                            }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Avatar circle with first letter
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) PrimaryBlue
                                        else Color.White.copy(alpha = 0.08f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = qari.name.take(1),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = qari.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${qari.style} • 114 Surah",
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
