package com.ammaricano.quran.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.ammaricano.quran.data.remote.FirebaseHelper
import com.ammaricano.quran.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var fontSizeArab by remember { mutableStateOf(3) }
    var fontSizeTranslation by remember { mutableStateOf(3) }
    var showLatin by remember { mutableStateOf(true) }
    var showTranslation by remember { mutableStateOf(true) }

    val arabSizeLabels = listOf("Sangat Kecil", "Kecil", "Standar", "Besar", "Sangat Besar")
    val translationSizeLabels = listOf("Sangat Kecil", "Kecil", "Standar", "Besar", "Sangat Besar")

    val arabFontSizeSp = when (fontSizeArab) {
        1 -> 20.sp
        2 -> 24.sp
        3 -> 28.sp
        4 -> 32.sp
        5 -> 36.sp
        else -> 28.sp
    }

    val transFontSizeSp = when (fontSizeTranslation) {
        1 -> 11.sp
        2 -> 12.sp
        3 -> 13.sp
        4 -> 15.sp
        5 -> 17.sp
        else -> 13.sp
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BgPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(BorderGlassTop)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = PrimaryBlue2,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Pengaturan Tampilan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                TextButton(
                    onClick = {
                        fontSizeArab = 3
                        fontSizeTranslation = 3
                        showLatin = true
                        showTranslation = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reset", color = TextMuted, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Preview Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceGlass)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Pratinjau Teks Ayat",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ",
                        fontSize = arabFontSizeSp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.End,
                        lineHeight = arabFontSizeSp * 1.6f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (showLatin) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bismillāhir-raḥmānir-raḥīm(i)",
                            fontSize = transFontSizeSp,
                            color = TextGold
                        )
                    }
                    if (showTranslation) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang.",
                            fontSize = transFontSizeSp,
                            color = TextSubtitle
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Arab font size slider
            Text(
                text = "Ukuran Tulisan Arab: ${arabSizeLabels[fontSizeArab - 1]}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Slider(
                value = fontSizeArab.toFloat(),
                onValueChange = { fontSizeArab = it.toInt() },
                valueRange = 1f..5f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = PrimaryBlue2,
                    activeTrackColor = PrimaryBlue,
                    inactiveTrackColor = SurfaceGlass
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Translation font size slider
            Text(
                text = "Ukuran Terjemahan: ${translationSizeLabels[fontSizeTranslation - 1]}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Slider(
                value = fontSizeTranslation.toFloat(),
                onValueChange = { fontSizeTranslation = it.toInt() },
                valueRange = 1f..5f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = PrimaryBlue2,
                    activeTrackColor = PrimaryBlue,
                    inactiveTrackColor = SurfaceGlass
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tampilkan Teks Latin",
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Switch(
                    checked = showLatin,
                    onCheckedChange = { showLatin = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextPrimary,
                        checkedTrackColor = PrimaryBlue,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = SurfaceGlass
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tampilkan Terjemahan",
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Switch(
                    checked = showTranslation,
                    onCheckedChange = { showTranslation = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextPrimary,
                        checkedTrackColor = PrimaryBlue,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = SurfaceGlass
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Simpan Pengaturan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
