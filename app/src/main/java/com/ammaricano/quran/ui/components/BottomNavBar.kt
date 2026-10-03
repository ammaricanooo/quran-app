package com.ammaricano.quran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted

sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Quran : NavItem("quran_home", "Home", Icons.Rounded.Home)
    object Jadwal : NavItem("jadwal", "Jadwal", Icons.Rounded.Schedule)
    object Lainnya : NavItem("lainnya_sheet", "Lainnya", Icons.Rounded.Menu)
    object Murottal : NavItem("murottal", "Murottal", Icons.Rounded.Headphones)
    object Profil : NavItem("bookmark", "Bookmark", Icons.Rounded.Bookmark)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onOpenLainnyaSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem.Quran,
        NavItem.Jadwal,
        NavItem.Lainnya,
        NavItem.Murottal,
        NavItem.Profil
    )

    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F1E36),
            Color(0xFF081224)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundBrush)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.10f),
                shape = shape
            )
            .padding(vertical = 8.dp, horizontal = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val contentColor = if (isSelected) PrimaryBlue2 else TextMuted

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            if (item == NavItem.Lainnya) {
                                onOpenLainnyaSheet()
                            } else {
                                onNavigate(item.route)
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = contentColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = item.title,
                        color = contentColor,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
