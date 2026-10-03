package com.ammaricano.quran.ui.screens.hadits

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.HaditsItem
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
fun HaditsScreen(
    viewModel: HaditsViewModel,
    onBack: () -> Unit
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 12.dp, end = 20.dp, bottom = 10.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
            }
            Column {
                Text("Hadits Arbain Nawawi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("42 Hadits Pokok Ajaran Islam", fontSize = 12.sp, color = TextMuted)
            }
        }

        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            GlassSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                placeholderText = "Cari nomor atau judul hadits..."
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            items(state.filteredList, key = { it.no }) { hadits ->
                HaditsCardItem(hadits = hadits)
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun HaditsCardItem(hadits: HaditsItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = hadits.no.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue2
                )
            }

            Text(
                text = hadits.judul,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic
        Text(
            text = hadits.arab,
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
            text = hadits.indo,
            fontSize = 12.sp,
            color = TextSubtitle,
            lineHeight = 19.sp
        )
    }
}
