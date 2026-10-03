package com.ammaricano.quran.ui.screens.detail

import android.content.Intent
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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.AyatItem
import com.ammaricano.quran.data.model.TafsirItem
import com.ammaricano.quran.service.AudioPlayerManager
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    viewModel: SurahDetailViewModel,
    audioPlayerManager: AudioPlayerManager,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showFontDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BgPrimary, BgPrimary2, BgPrimary)
                )
            )
    ) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 12.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Kembali",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = state.surahDetail?.namaLatin ?: "Detail Surah",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    state.surahDetail?.let {
                        Text(
                            text = "${it.tempatTurun} • ${it.jumlahAyat} Ayat",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            IconButton(onClick = { showFontDialog = true }) {
                Icon(
                    imageVector = Icons.Rounded.FormatSize,
                    contentDescription = "Ukuran Font",
                    tint = PrimaryBlue2
                )
            }
        }

        // Switch Tabs: Ayat vs Tafsir
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .padding(3.dp)
        ) {
            val isAyat = state.selectedTab == "ayat"

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isAyat) PrimaryBlue else Color.Transparent)
                    .clickable { viewModel.selectTab("ayat") }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Baca Ayat",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAyat) Color.White else TextMuted
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (!isAyat) PrimaryBlue else Color.Transparent)
                    .clickable { viewModel.selectTab("tafsir") }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tafsir Kemenag",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isAyat) Color.White else TextMuted
                )
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
                        Text(
                            text = "Memuat ayat Al-Qur'an...",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                state.surahDetail != null -> {
                    val detail = state.surahDetail!!

                    if (state.selectedTab == "ayat") {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Hero Surah Card
                            item {
                                SurahHeroBanner(
                                    detail = detail,
                                    onPlayFullAudio = {
                                        val audioUrl = detail.audioFull?.get("05")
                                            ?: detail.audioFull?.values?.firstOrNull()
                                        if (audioUrl != null) {
                                            audioPlayerManager.playAudio(
                                                url = audioUrl,
                                                title = "Surah ${detail.namaLatin}",
                                                subtitle = "Murottal Full Surah"
                                            )
                                        }
                                    }
                                )
                            }

                            // Bismillah
                            if (detail.nomor != 9 && detail.nomor != 1) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
                                            fontFamily = AyatFontFamily,
                                            fontSize = state.arabicFontSize.sp,
                                            color = TextPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            // Ayat Cards
                            items(detail.ayat, key = { it.nomorAyat }) { ayat ->
                                val isBookmarked = state.bookmarkedAyatNumbers.contains(ayat.nomorAyat)
                                AyatCardItem(
                                    ayat = ayat,
                                    fontSizeArab = state.arabicFontSize,
                                    isBookmarked = isBookmarked,
                                    onBookmark = { viewModel.toggleBookmarkAyat(ayat) },
                                    onPlayAudio = {
                                        val audioUrl = ayat.audio?.get("05")
                                            ?: ayat.audio?.values?.firstOrNull()
                                        if (audioUrl != null) {
                                            audioPlayerManager.playAudio(
                                                url = audioUrl,
                                                title = "${detail.namaLatin} : ${ayat.nomorAyat}",
                                                subtitle = "Ayat ${ayat.nomorAyat}"
                                            )
                                        }
                                    },
                                    onShare = {
                                        val shareText = "${ayat.teksArab}\n\n\"${ayat.teksIndonesia}\"\n(QS. ${detail.namaLatin}: ${ayat.nomorAyat})"
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, shareText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Bagikan Ayat"))
                                    }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    } else {
                        // Tafsir Tab
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(state.tafsirList, key = { it.ayat }) { tafsir ->
                                TafsirCardItem(tafsir = tafsir)
                            }
                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // Font size setting bottom sheet
    if (showFontDialog) {
        ModalBottomSheet(
            onDismissRequest = { showFontDialog = false },
            containerColor = BgPrimary2,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Ukuran Font Ayat Arab",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
                    fontFamily = AyatFontFamily,
                    fontSize = state.arabicFontSize.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Slider(
                    value = state.arabicFontSize,
                    onValueChange = { viewModel.setArabicFontSize(it) },
                    valueRange = 20f..38f,
                    colors = SliderDefaults.colors(
                        thumbColor = PrimaryBlue,
                        activeTrackColor = PrimaryBlue2
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Kecil (20sp)", fontSize = 11.sp, color = TextMuted)
                    Text("Besar (38sp)", fontSize = 11.sp, color = TextMuted)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SurahHeroBanner(
    detail: com.ammaricano.quran.data.model.SurahDetailData,
    onPlayFullAudio: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        PrimaryBlue,
                        PrimaryBlue2
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = detail.namaLatin,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = detail.arti,
                fontSize = 13.sp,
                color = TextSubtitle,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = Color.White.copy(alpha = 0.15f),
                thickness = 1.dp,
                modifier = Modifier.width(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${detail.tempatTurun.uppercase()} • ${detail.jumlahAyat} AYAT",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryBlue2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Play Murottal Button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PrimaryBlue)
                    .clickable { onPlayFullAudio() }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Putar Murottal Full",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AyatCardItem(
    ayat: AyatItem,
    fontSizeArab: Float,
    isBookmarked: Boolean,
    onBookmark: () -> Unit,
    onPlayAudio: () -> Unit,
    onShare: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ayat.nomorAyat.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue2
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onPlayAudio, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "Putar Audio",
                        tint = PrimaryBlue2,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onBookmark, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) PrimaryBlue2 else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Arabic Verse with dynamic font size
        Text(
            text = ayat.teksArab,
            fontFamily = AyatFontFamily,
            fontSize = fontSizeArab.sp,
            lineHeight = (fontSizeArab * 1.8f).sp,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Transliteration Latin
        Text(
            text = ayat.teksLatin,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = PrimaryBlue2
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Indonesian Translation
        Text(
            text = ayat.teksIndonesia,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = TextSubtitle
        )
    }
}

@Composable
fun TafsirCardItem(tafsir: TafsirItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tafsir.ayat.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue2
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Tafsir Ayat ke-${tafsir.ayat}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = tafsir.teks,
            fontSize = 13.sp,
            lineHeight = 22.sp,
            color = TextSubtitle
        )
    }
}
