package com.ammaricano.quran.ui.screens.kuis

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class QuizQuestion(
    val id: String,
    val question: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class KuisUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedChoice: Int? = null,
    val isAnswered: Boolean = false,
    val score: Int = 0,
    val isFinished: Boolean = false
)

class KuisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KuisUiState())
    val uiState: StateFlow<KuisUiState> = _uiState.asStateFlow()

    private val sampleQuestions = listOf(
        QuizQuestion(
            id = "q-1",
            question = "Surah apakah yang disebut sebagai Ummul Qur'an (Induk Al-Qur'an)?",
            choices = listOf("Al-Baqarah", "Al-Fatihah", "Yasin", "Al-Ikhlas"),
            correctIndex = 1,
            explanation = "Surah Al-Fatihah disebut Ummul Qur'an karena memuat inti pokok ajaran seluruh isi Al-Qur'an."
        ),
        QuizQuestion(
            id = "q-2",
            question = "Berapa jumlah surah yang terdapat di dalam kitab suci Al-Qur'an?",
            choices = listOf("110 Surah", "112 Surah", "114 Surah", "116 Surah"),
            correctIndex = 2,
            explanation = "Al-Qur'an terdiri dari 114 surah yang terbagi ke dalam 30 Juz."
        ),
        QuizQuestion(
            id = "q-3",
            question = "Surah terpanjang di dalam Al-Qur'an adalah surah...",
            choices = listOf("Ali 'Imran", "An-Nisa", "Al-Ma'idah", "Al-Baqarah"),
            correctIndex = 3,
            explanation = "Surah Al-Baqarah adalah surah terpanjang dengan jumlah 286 ayat."
        ),
        QuizQuestion(
            id = "q-4",
            question = "Surah yang tidak diawali dengan bacaan Basmalah adalah surah...",
            choices = listOf("At-Taubah", "Al-Anfal", "Al-Kahf", "Yasin"),
            correctIndex = 0,
            explanation = "Surah At-Taubah (Bara'ah) tidak diawali Basmalah karena bernada tegas memutuskan perjanjian dengan kaum musyrikin."
        ),
        QuizQuestion(
            id = "q-5",
            question = "Ayat Kursi terdapat di dalam Surah...",
            choices = listOf("Al-Baqarah ayat 255", "Al-Baqarah ayat 286", "Ali 'Imran ayat 190", "Al-Kahf ayat 10"),
            correctIndex = 0,
            explanation = "Ayat Kursi merupakan ayat ke-255 dari Surah Al-Baqarah yang memiliki keagungan luar biasa."
        )
    )

    init {
        resetQuiz()
    }

    fun resetQuiz() {
        _uiState.value = KuisUiState(questions = sampleQuestions)
    }

    fun submitAnswer(choiceIndex: Int) {
        val current = _uiState.value
        if (current.isAnswered) return

        val currentQ = current.questions[current.currentIndex]
        val isCorrect = choiceIndex == currentQ.correctIndex
        val newScore = if (isCorrect) current.score + 20 else current.score

        _uiState.value = current.copy(
            selectedChoice = choiceIndex,
            isAnswered = true,
            score = newScore
        )
    }

    fun nextQuestion() {
        val current = _uiState.value
        if (current.currentIndex + 1 < current.questions.size) {
            _uiState.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedChoice = null,
                isAnswered = false
            )
        } else {
            _uiState.value = current.copy(isFinished = true)
        }
    }
}
