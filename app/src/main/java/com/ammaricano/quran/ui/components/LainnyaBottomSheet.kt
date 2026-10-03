package com.ammaricano.quran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Gamepad
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Scroll
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary

data class ExtraMenuItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val iconColor: Color,
    val bgGradient: List<Color>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LainnyaBottomSheet(
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val menuItems = listOf(
        ExtraMenuItem(
            title = "Juz 1-30",
            icon = Icons.Rounded.Layers,
            route = "juz",
            iconColor = PrimaryBlue2,
            bgGradient = listOf(Color(0x331089FF), Color(0x111089FF))
        ),
        ExtraMenuItem(
            title = "Dzikir",
            icon = Icons.Rounded.Favorite,
            route = "dzikir",
            iconColor = Color(0xFF2DD4BF),
            bgGradient = listOf(Color(0x332DD4BF), Color(0x112DD4BF))
        ),
        ExtraMenuItem(
            title = "Doa Harian",
            icon = Icons.Rounded.VolunteerActivism,
            route = "doa",
            iconColor = PrimaryBlue2,
            bgGradient = listOf(Color(0x331089FF), Color(0x111089FF))
        ),
        ExtraMenuItem(
            title = "Hadits Arbain",
            icon = Icons.Rounded.FormatQuote,
            route = "hadits",
            iconColor = Color(0xFFFB7185),
            bgGradient = listOf(Color(0x33FB7185), Color(0x11FB7185))
        ),
        ExtraMenuItem(
            title = "Asmaul Husna",
            icon = Icons.Rounded.AutoAwesome,
            route = "asmaul_husna",
            iconColor = Color(0xFFFBBF24),
            bgGradient = listOf(Color(0x33FBBF24), Color(0x11FBBF24))
        ),
        ExtraMenuItem(
            title = "Tahlil & Yasin",
            icon = Icons.Rounded.Scroll,
            route = "tahlil",
            iconColor = Color(0xFFC084FC),
            bgGradient = listOf(Color(0x33C084FC), Color(0x11C084FC))
        ),
        ExtraMenuItem(
            title = "Kuis Qur'an",
            icon = Icons.Rounded.Gamepad,
            route = "kuis",
            iconColor = Color(0xFF34D399),
            bgGradient = listOf(Color(0x3334D399), Color(0x1134D399))
        ),
        ExtraMenuItem(
            title = "Tafsir Kemenag",
            icon = Icons.Rounded.MenuBook,
            route = "quran_home",
            iconColor = Color(0xFF38BDF8),
            bgGradient = listOf(Color(0x3338BDF8), Color(0x1138BDF8))
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BgPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Menu & Fitur Lainnya",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Jelajahi seluruh fitur islami Al-Qur'an Ku",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Tutup", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.verticalGradient(item.bgGradient))
                            .border(1.dp, item.iconColor.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                            .clickable {
                                onDismiss()
                                onNavigate(item.route)
                            }
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(item.iconColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
