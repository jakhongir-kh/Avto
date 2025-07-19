package com.uztest.avto.presentation.screen.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uztest.avto.domain.model.Question
import com.uztest.avto.domain.model.TestResult
import com.uztest.avto.domain.usecase.GetTestQuestionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestViewModel(
    private val getTestQuestionsUseCase: GetTestQuestionsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TestUiState())
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()
    
    private var startTime: Long = 0
    
    fun loadTest(categoryId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val questions = getTestQuestionsUseCase(categoryId)
                startTime = System.currentTimeMillis()
                _uiState.value = _uiState.value.copy(
                    questions = questions,
                    categoryId = categoryId,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    fun selectAnswer(answerIndex: Int) {
        val currentState = _uiState.value
        if (currentState.selectedAnswer != null || currentState.showResult) return
        
        _uiState.value = currentState.copy(
            selectedAnswer = answerIndex,
            showResult = true
        )
    }
    
    fun nextQuestion() {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions[currentState.currentQuestionIndex]
        val isCorrect = currentState.selectedAnswer == currentQuestion.correctAnswerIndex
        
        val newCorrectAnswers = if (isCorrect) currentState.correctAnswers + 1 else currentState.correctAnswers
        val newQuestionIndex = currentState.currentQuestionIndex + 1
        
        if (newQuestionIndex >= currentState.questions.size) {
            // Test completed
            val timeSpent = System.currentTimeMillis() - startTime
            val testResult = TestResult(
                categoryId = currentState.categoryId,
                totalQuestions = currentState.questions.size,
                correctAnswers = newCorrectAnswers,
                timeSpent = timeSpent
            )
            
            _uiState.value = currentState.copy(
                correctAnswers = newCorrectAnswers,
                isCompleted = true,
                testResult = testResult
            )
        } else {
            _uiState.value = currentState.copy(
                currentQuestionIndex = newQuestionIndex,
                correctAnswers = newCorrectAnswers,
                selectedAnswer = null,
                showResult = false
            )
        }
    }
    
    fun restartTest() {
        val currentState = _uiState.value
        startTime = System.currentTimeMillis()
        _uiState.value = currentState.copy(
            currentQuestionIndex = 0,
            correctAnswers = 0,
            selectedAnswer = null,
            showResult = false,
            isCompleted = false,
            testResult = null,
            questions = currentState.questions.shuffled()
        )
    }
}

data class TestUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val selectedAnswer: Int? = null,
    val showResult: Boolean = false,
    val isCompleted: Boolean = false,
    val testResult: TestResult? = null,
    val categoryId: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)