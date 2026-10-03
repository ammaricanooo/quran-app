package com.ammaricano.quran.ui.screens.kuis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.ammaricano.quran.ui.components.GlassCard
import com.ammaricano.quran.ui.theme.BgPrimary
import com.ammaricano.quran.ui.theme.BgPrimary2
import com.ammaricano.quran.ui.theme.EmeraldGreen
import com.ammaricano.quran.ui.theme.PrimaryBlue
import com.ammaricano.quran.ui.theme.PrimaryBlue2
import com.ammaricano.quran.ui.theme.TextMuted
import com.ammaricano.quran.ui.theme.TextPrimary
import com.ammaricano.quran.ui.theme.TextSubtitle

@Composable
fun KuisScreen(
    viewModel: KuisViewModel,
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
                Text("Kuis Wawasan Al-Qur'an", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Uji pemahaman dan ilmu Al-Qur'an Anda", fontSize = 12.sp, color = TextMuted)
            }
        }

        if (state.isFinished) {
            // Result Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBBF24).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Kuis Selesai!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Skor Anda: ${state.score} / 100",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue2
                )

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.resetQuiz() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Main Lagi")
                }
            }
        } else if (state.questions.isNotEmpty()) {
            val q = state.questions[state.currentIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                // Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pertanyaan ${state.currentIndex + 1} dari ${state.questions.size}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Skor: ${state.score}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue2
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question Card
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = q.question,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Choices
                q.choices.forEachIndexed { index, choice ->
                    val isSelected = state.selectedChoice == index
                    val isCorrect = index == q.correctIndex

                    val borderColor = when {
                        state.isAnswered && isCorrect -> EmeraldGreen
                        state.isAnswered && isSelected && !isCorrect -> Color(0xFFEF4444)
                        isSelected -> PrimaryBlue
                        else -> Color.White.copy(alpha = 0.10f)
                    }

                    val bgColor = when {
                        state.isAnswered && isCorrect -> EmeraldGreen.copy(alpha = 0.2f)
                        state.isAnswered && isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.2f)
                        else -> Color.White.copy(alpha = 0.04f)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                            .clickable(enabled = !state.isAnswered) {
                                viewModel.submitAnswer(index)
                            }
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = choice,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )

                            if (state.isAnswered) {
                                if (isCorrect) {
                                    Icon(Icons.Rounded.Check, contentDescription = null, tint = EmeraldGreen)
                                } else if (isSelected) {
                                    Icon(Icons.Rounded.Close, contentDescription = null, tint = Color(0xFFEF4444))
                                }
                            }
                        }
                    }
                }

                // Explanation
                if (state.isAnswered) {
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Penjelasan:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = q.explanation,
                            fontSize = 12.sp,
                            color = TextSubtitle,
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.nextQuestion() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = if (state.currentIndex + 1 < state.questions.size) "Pertanyaan Selanjutnya" else "Lihat Hasil Akhir",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
