package com.ammaricano.quran.ui.screens.doa

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.DoaItem
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.components.GlassSearchBar
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@Composable
fun DoaScreen(viewModel: DoaViewModel) {
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
                text = "Kumpulan Doa",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Doa-doa Harian Pilihan & Sunnah",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            GlassSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                placeholderText = "Cari judul atau isi doa..."
            )
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
                        Text(text = "Memuat daftar doa...", color = TextMuted, fontSize = 13.sp)
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.errorMessage ?: "", color = TextMuted, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.loadDoa() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Rounded.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Coba Lagi")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.filteredList, key = { it.id }) { doa ->
                            DoaExpandableCard(doa = doa)
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
fun DoaExpandableCard(doa: DoaItem) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = doa.nama,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        val shareText = "${doa.nama}\n\n${doa.ar}\n\n\"${doa.idn}\""
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Bagikan Doa"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Share",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = null,
                    tint = PrimaryBlue2
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                // Arabic
                Text(
                    text = doa.ar,
                    fontFamily = AyatFontFamily,
                    fontSize = 22.sp,
                    lineHeight = 42.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                // Transliteration if available
                doa.tr?.let { tr ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = tr,
                        fontSize = 12.sp,
                        color = PrimaryBlue2,
                        lineHeight = 18.sp
                    )
                }

                // Translation
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"${doa.idn}\"",
                    fontSize = 12.sp,
                    color = TextSubtitle,
                    lineHeight = 18.sp
                )

                doa.tentang?.let { tentang ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Keterangan: $tentang",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
