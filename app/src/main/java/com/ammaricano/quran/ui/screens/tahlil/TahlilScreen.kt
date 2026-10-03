package com.ammaricano.quran.ui.screens.tahlil

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.TahlilItem
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.AyatFontFamily
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@Composable
fun TahlilScreen(
    viewModel: TahlilViewModel,
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
                Text("Tahlil & Doa Arwah", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Susunan bacaan tahlil lengkap", fontSize = 12.sp, color = TextMuted)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize().weight(1f)
        ) {
            itemsIndexed(state.items, key = { _, item -> item.id }) { index, item ->
                TahlilCardItem(index = index + 1, item = item)
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun TahlilCardItem(index: Int, item: TahlilItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index. ${item.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue2,
                modifier = Modifier.weight(1f)
            )
            item.repeat?.let { repeat ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryBlue.copy(alpha = 0.20f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = repeat, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue2)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Arabic
        Text(
            text = item.arabic,
            fontFamily = AyatFontFamily,
            fontSize = 22.sp,
            lineHeight = 42.sp,
            color = TextPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Latin
        Text(text = item.latin, fontSize = 12.sp, color = PrimaryBlue2, lineHeight = 18.sp)

        Spacer(modifier = Modifier.height(6.dp))

        // Indonesian translation
        Text(text = item.translation, fontSize = 12.sp, color = TextSubtitle, lineHeight = 18.sp)
    }
}
