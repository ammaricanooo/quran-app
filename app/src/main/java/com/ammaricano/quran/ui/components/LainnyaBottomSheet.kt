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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2

data class WebMenuItem(
    val name: String,
    val icon: ImageVector,
    val route: String,
    val iconColor: Color,
    val bgTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LainnyaBottomSheet(
    onDismiss: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Exactly matching extraMenuItems in web Navbar.tsx
    val menuItems = listOf(
        WebMenuItem("Juz", Icons.Rounded.Layers, "juz", PrimaryBlue2, PrimaryBlue.copy(alpha = 0.12f)),
        WebMenuItem("Dzikir", Icons.Rounded.Air, "dzikir", Color(0xFF2DD4BF), Color(0xFF2DD4BF).copy(alpha = 0.12f)),
        WebMenuItem("Doa", Icons.Rounded.MenuBook, "doa", PrimaryBlue2, PrimaryBlue.copy(alpha = 0.12f)),
        WebMenuItem("Kiblat", Icons.Rounded.Explore, "kiblat", Color(0xFF38BDF8), Color(0xFF38BDF8).copy(alpha = 0.12f)),
        WebMenuItem("Hadits", Icons.Rounded.FormatQuote, "hadits", Color(0xFFFB7185), Color(0xFFFB7185).copy(alpha = 0.12f)),
        WebMenuItem("Asmaul Husna", Icons.Rounded.AutoAwesome, "asmaul_husna", Color(0xFFFBBF24), Color(0xFFFBBF24).copy(alpha = 0.12f)),
        WebMenuItem("Tahlil", Icons.Rounded.Description, "tahlil", Color(0xFFC084FC), Color(0xFFC084FC).copy(alpha = 0.12f)),
        WebMenuItem("Robithoh", Icons.Rounded.VolunteerActivism, "robithoh", Color(0xFF2DD4BF), Color(0xFF2DD4BF).copy(alpha = 0.12f)),
        WebMenuItem("Kultum", Icons.Rounded.Mic, "kultum", Color(0xFFF59E0B), Color(0xFFF59E0B).copy(alpha = 0.12f)),
        WebMenuItem("Artikel", Icons.Rounded.Newspaper, "artikel", PrimaryBlue2, PrimaryBlue.copy(alpha = 0.12f)),
        WebMenuItem("Maulid", Icons.Rounded.AutoStories, "maulid", Color(0xFFFB7185), Color(0xFFFB7185).copy(alpha = 0.12f)),
        WebMenuItem("Kuis", Icons.Rounded.Gamepad, "kuis", Color(0xFF34D399), Color(0xFF34D399).copy(alpha = 0.12f))
    )

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
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MENU LAINNYA",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.85f),
                    letterSpacing = 1.5.sp
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

            // Pengaturan Tampilan Banner (matching web exactly)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryBlue.copy(alpha = 0.15f))
                    .border(1.dp, PrimaryBlue.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                    .clickable {
                        onDismiss()
                        onNavigate("settings")
                    }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryBlue.copy(alpha = 0.25f)),
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
                    Column {
                        Text(
                            text = "Pengaturan Tampilan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Ukuran teks Arab, Latin & Terjemahan",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.50f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryBlue.copy(alpha = 0.25f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Buka",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue2
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 12 Menu Items Grid (3 columns)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItems) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(item.bgTint)
                            .border(1.dp, item.iconColor.copy(alpha = 0.22f), RoundedCornerShape(22.dp))
                            .clickable {
                                onDismiss()
                                onNavigate(item.route)
                            }
                            .padding(vertical = 16.dp, horizontal = 6.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = item.iconColor,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.90f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
