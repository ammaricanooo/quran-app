package com.ammaricano.quran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ammaricano.quran.ui.theme.BorderGlass
import com.ammaricano.quran.ui.theme.BorderGlassTop

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderAlpha: Float = 0.12f,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.06f),
            Color.White.copy(alpha = 0.02f)
        )
    )
    val borderBrush = Brush.verticalGradient(
        colors = listOf(
            BorderGlassTop,
            BorderGlass.copy(alpha = borderAlpha)
        )
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundBrush)
            .border(width = 1.dp, brush = borderBrush, shape = shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Column {
            content()
        }
    }
}
