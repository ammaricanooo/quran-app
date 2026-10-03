package com.ammaricano.quran.ui.screens.maulid

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.ui.theme.*

@Composable
fun MaulidScreen(
    viewModel: MaulidViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BgPrimary, BgPrimary2, BgPrimary)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (state.selectedSubcategory != null) {
                            viewModel.selectSubcategory(null)
                        } else if (state.selectedKitab != null) {
                            viewModel.selectKitab(null)
                        } else {
                            onBackClick()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Kembali",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when {
                            state.selectedSubcategory != null -> state.selectedSubcategory!!.name
                            state.selectedKitab != null -> state.selectedKitab!!.name
                            else -> "Kumpulan Maulid"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = when {
                            state.selectedSubcategory != null -> "${state.selectedKitab?.name} • ${state.selectedSubcategory!!.readings.size} Bacaan"
                            state.selectedKitab != null -> "${state.selectedKitab!!.subcategories.size} Bab / Fasal"
                            else -> "${state.kitabs.size} Kitab Maulid Nabi ﷺ"
                        },
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                if (state.selectedSubcategory != null) {
                    IconButton(
                        onClick = {
                            val sub = state.selectedSubcategory!!
                            val shareText = buildString {
                                appendLine("${sub.name} - ${state.selectedKitab?.name}")
                                appendLine()
                                sub.readings.forEach { r ->
                                    appendLine(r.arabic)
                                    appendLine(r.transliteration)
                                    appendLine(r.translate)
                                    appendLine()
                                }
                                appendLine("Dibagikan dari Al-Qur'an Ku")
                            }
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Bagikan Maulid"))
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceGlass)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Body Content based on navigation level
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else when {
                // LEVEL 3: Reading verses in Subcategory
                state.selectedSubcategory != null -> {
                    val sub = state.selectedSubcategory!!
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (sub.description.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(PrimaryBlue.copy(alpha = 0.1f))
                                        .border(1.dp, PrimaryBlue.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = sub.description,
                                        fontSize = 12.sp,
                                        color = TextSubtitle,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        items(sub.readings, key = { it.id }) { reading ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .border(1.dp, BorderGlass, RoundedCornerShape(18.dp)),
                                colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(PrimaryBlue.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${reading.order}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryBlue2
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = reading.arabic,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.End,
                                        lineHeight = 42.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    if (reading.transliteration.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = reading.transliteration,
                                            fontSize = 13.sp,
                                            color = TextGold.copy(alpha = 0.9f),
                                            lineHeight = 19.sp
                                        )
                                    }

                                    if (reading.translate.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = reading.translate,
                                            fontSize = 13.sp,
                                            color = TextSubtitle,
                                            lineHeight = 19.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // LEVEL 2: Subcategories / Chapters in Kitab
                state.selectedKitab != null -> {
                    val kitab = state.selectedKitab!!
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(kitab.subcategories, key = { it.id }) { sub ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .border(1.dp, BorderGlass, RoundedCornerShape(18.dp))
                                    .clickable { viewModel.selectSubcategory(sub) },
                                colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(PrimaryBlue.copy(alpha = 0.2f), SecondaryPurple.copy(alpha = 0.2f))
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.MenuBook,
                                            contentDescription = null,
                                            tint = PrimaryBlue2,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sub.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${sub.readings.size} bait bacaan",
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Rounded.ChevronRight,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // LEVEL 1: Kitab List
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                PrimaryBlue.copy(alpha = 0.25f),
                                                SecondaryPurple.copy(alpha = 0.25f)
                                            )
                                        )
                                    )
                                    .border(1.dp, BorderGlassTop, RoundedCornerShape(20.dp))
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryBlue.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.AutoStories,
                                                contentDescription = null,
                                                tint = TextGold,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Kitab Maulid Nabi ﷺ",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Kumpulan syair, sejarah kenabian, dan pujian shalawat agung kepada Rasulullah SAW dari para ulama terkemuka.",
                                        fontSize = 13.sp,
                                        color = TextSubtitle,
                                        lineHeight = 19.sp
                                    )
                                }
                            }
                        }

                        items(state.kitabs, key = { it.id }) { kitab ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, BorderGlass, RoundedCornerShape(20.dp))
                                    .clickable { viewModel.selectKitab(kitab) },
                                colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(PrimaryBlue.copy(alpha = 0.3f), SecondaryPurple.copy(alpha = 0.3f))
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AutoStories,
                                            contentDescription = null,
                                            tint = PrimaryBlue2,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = kitab.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "${kitab.subcategories.size} Bab • Lengkap Terjemah",
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(SurfaceGlass)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowForward,
                                            contentDescription = null,
                                            tint = PrimaryBlue2,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
