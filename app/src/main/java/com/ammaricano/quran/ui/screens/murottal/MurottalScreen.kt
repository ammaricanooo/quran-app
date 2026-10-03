package com.ammaricano.quran.ui.screens.murottal

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
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.SurahItem
import com.ammaricano.quran.service.AudioPlayerManager
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.SecondaryPurple
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@Composable
fun MurottalScreen(
    viewModel: MurottalViewModel,
    audioPlayerManager: AudioPlayerManager
) {
    val state by viewModel.uiState.collectAsState()
    val playbackState by audioPlayerManager.playbackState.collectAsState()

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
                text = "Murottal Al-Qur'an",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Lantunan Murottal 30 Juz dari Qari Terkemuka",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Qari Picker Horizontal Row
            Text(text = "PILIH QARI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue2)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.qariList) { qari ->
                    val isSelected = state.selectedQari.id == qari.id

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryBlue else Color.White.copy(alpha = 0.05f))
                            .border(
                                1.dp,
                                if (isSelected) PrimaryBlue else Color.White.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .clickable { viewModel.selectQari(qari) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = qari.name,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Active Player Card
        state.selectedSurah?.let { surah ->
            val audioUrl = viewModel.getAudioUrl(surah, state.selectedQari.id)
            val isCurrentSurahPlaying = playbackState.currentUrl == audioUrl && playbackState.isPlaying

            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = surah.namaLatin,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${surah.arti} • ${surah.jumlahAyat} Ayat",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                            Text(
                                text = state.selectedQari.name,
                                fontSize = 11.sp,
                                color = PrimaryBlue2,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Play/Pause Big Button
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue)
                                .clickable {
                                    if (audioUrl != null) {
                                        audioPlayerManager.playAudio(
                                            url = audioUrl,
                                            title = "Surah ${surah.namaLatin}",
                                            subtitle = state.selectedQari.name
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isCurrentSurahPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        // Surah List to Play
        Box(modifier = Modifier.fillMaxSize().weight(1f)) {
            if (state.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = PrimaryBlue2)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.surahs, key = { it.nomor }) { surah ->
                        val isSelected = state.selectedSurah?.nomor == surah.nomor

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.selectSurah(surah)
                                val url = viewModel.getAudioUrl(surah, state.selectedQari.id)
                                if (url != null) {
                                    audioPlayerManager.playAudio(
                                        url = url,
                                        title = "Surah ${surah.namaLatin}",
                                        subtitle = state.selectedQari.name
                                    )
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isSelected) PrimaryBlue.copy(alpha = 0.3f)
                                                else Color.White.copy(alpha = 0.05f)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = surah.nomor.toString(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) PrimaryBlue2 else TextMuted
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = surah.namaLatin,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${surah.tempatTurun} • ${surah.jumlahAyat} Ayat",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Text(
                                    text = surah.nama,
                                    fontFamily = AyatFontFamily,
                                    fontSize = 18.sp,
                                    color = if (isSelected) PrimaryBlue2 else TextSubtitle
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}
