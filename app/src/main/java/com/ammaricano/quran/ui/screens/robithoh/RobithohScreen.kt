package com.ammaricano.quran.ui.screens.robithoh

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.data.model.RobithohItem
import com.ammaricano.quran.ui.theme.*

@Composable
fun RobithohScreen(
    viewModel: RobithohViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

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
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
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
                        text = "Doa Rabithah",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Al-Ma'tsurat Sugro & Pengikat Hati",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // List of Robithoh Items
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Info Card
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
                                        imageVector = Icons.Rounded.Favorite,
                                        contentDescription = null,
                                        tint = TextGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Keutamaan Doa Rabithah",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Doa yang diajarkan oleh Imam Hasan Al-Banna untuk mengikat hati kaum beriman dalam ketaatan kepada Allah, mempererat persaudaraan, dan menguatkan ikatan dakwah.",
                                fontSize = 13.sp,
                                color = TextSubtitle,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                items(state.items, key = { it.id }) { item ->
                    RobithohCard(
                        item = item,
                        currentCount = state.counts[item.id] ?: 0,
                        onIncrement = { viewModel.incrementCount(item.id, item.ulang) },
                        onReset = { viewModel.resetCount(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RobithohCard(
    item: RobithohItem,
    currentCount: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit
) {
    val targetCount = item.ulang.replace("x", "").toIntOrNull() ?: 1
    val isCompleted = currentCount >= targetCount

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (isCompleted) EmeraldGreen.copy(alpha = 0.6f) else BorderGlass,
                RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) EmeraldGreen.copy(alpha = 0.08f) else SurfaceGlass
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top row: Title and Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Dibaca ${item.ulang}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlue2
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Arabic text
            Text(
                text = item.arab,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.End,
                lineHeight = 42.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Transliteration
            Text(
                text = item.latin,
                fontSize = 13.sp,
                color = TextGold.copy(alpha = 0.9f),
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Translation
            Text(
                text = item.indo,
                fontSize = 13.sp,
                color = TextSubtitle,
                lineHeight = 19.sp
            )

            // Faedah box
            if (item.faedah.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF).copy(alpha = 0.04f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "💡 Faedah: ${item.faedah}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Counter & Reset controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.sumber,
                    fontSize = 11.sp,
                    color = TextMuted
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentCount > 0) {
                        IconButton(
                            onClick = onReset,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFFFFF).copy(alpha = 0.06f))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Reset",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Button(
                        onClick = onIncrement,
                        enabled = !isCompleted,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) EmeraldGreen else PrimaryBlue,
                            disabledContainerColor = EmeraldGreen.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Rounded.Check else Icons.Rounded.TouchApp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCompleted) "Selesai ($currentCount/$targetCount)" else "$currentCount / $targetCount",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
