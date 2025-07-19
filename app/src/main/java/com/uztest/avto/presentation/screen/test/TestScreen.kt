package com.uztest.avto.presentation.screen.test

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uztest.avto.data.repository.QuestionRepositoryImpl
import com.uztest.avto.domain.model.Question
import com.uztest.avto.domain.model.TestResult
import com.uztest.avto.domain.usecase.GetTestQuestionsUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    categoryId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TestViewModel = viewModel {
        TestViewModel(GetTestQuestionsUseCase(QuestionRepositoryImpl()))
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(categoryId) {
        viewModel.loadTest(categoryId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = {
                if (!uiState.isCompleted) {
                    Text(
                        text = "${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        text = "Test Results",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Outlined.ArrowBack, 
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            uiState.isCompleted -> {
                TestResultScreen(
                    testResult = uiState.testResult!!,
                    onRestartTest = { viewModel.restartTest() },
                    onNavigateBack = onNavigateBack
                )
            }
            
            uiState.questions.isNotEmpty() -> {
                TestQuestionScreen(
                    question = uiState.questions[uiState.currentQuestionIndex],
                    selectedAnswer = uiState.selectedAnswer,
                    showResult = uiState.showResult,
                    onAnswerSelected = { viewModel.selectAnswer(it) },
                    onNextQuestion = { viewModel.nextQuestion() },
                    progress = (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size
                )
            }
        }
    }
}

@Composable
private fun TestQuestionScreen(
    question: Question,
    selectedAnswer: Int?,
    showResult: Boolean,
    onAnswerSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    progress: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF64B5F6),
                                Color(0xFF42A5F5)
                            )
                        )
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Question
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color.Black.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(28.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = MaterialTheme.typography.headlineMedium.lineHeight * 1.2
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Options
        question.options.forEachIndexed { index, option ->
            AnswerOption(
                text = option,
                isSelected = selectedAnswer == index,
                isCorrect = index == question.correctAnswerIndex,
                showResult = showResult,
                onClick = { onAnswerSelected(index) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Next button
        AnimatedVisibility(
            visible = showResult,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showResult && isCorrect -> Color(0xFFE8F5E8) // Pastel Green
        showResult && isSelected && !isCorrect -> Color(0xFFFCE4EC) // Pastel Pink
        isSelected -> Color(0xFFE3F2FD) // Pastel Blue
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        showResult && isCorrect -> Color(0xFF81C784) // Soft Green
        showResult && isSelected && !isCorrect -> Color(0xFFF48FB1) // Soft Pink
        isSelected -> Color(0xFF90CAF9) // Soft Blue
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }
    
    val textColor = when {
        showResult && isCorrect -> Color(0xFF2E7D32) // Deep Green
        showResult && isSelected && !isCorrect -> Color(0xFFC2185B) // Deep Pink
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected || showResult) 3.dp else 1.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .clickable(enabled = !showResult) { onClick() }
            .border(
                width = if (isSelected || showResult) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            
            if (showResult) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isCorrect) Color(0xFF81C784) 
                            else if (isSelected) Color(0xFFF48FB1) 
                            else Color.Transparent
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCorrect || (isSelected && !isCorrect)) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Outlined.Check else Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TestResultScreen(
    testResult: TestResult,
    onRestartTest: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Result icon
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(70.dp))
                .background(
                    if (testResult.isPassed) Color(0xFFE8F5E8) // Pastel Green
                    else Color(0xFFFCE4EC) // Pastel Pink
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (testResult.isPassed) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                contentDescription = null,
                modifier = Modifier.size(70.dp),
                tint = if (testResult.isPassed) Color(0xFF81C784) else Color(0xFFF48FB1)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Result text
        Text(
            text = if (testResult.isPassed) "Well Done!" else "Keep Learning!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Light,
            color = if (testResult.isPassed) Color(0xFF81C784) else Color(0xFFF48FB1)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "You scored ${testResult.percentage}%",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                ResultStat("Correct Answers", "${testResult.correctAnswers}/${testResult.totalQuestions}")
                Spacer(modifier = Modifier.height(16.dp))
                ResultStat("Accuracy", "${testResult.percentage}%")
                Spacer(modifier = Modifier.height(16.dp))
                ResultStat("Time Taken", "${testResult.timeSpent / 1000}s")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Buttons
        Button(
            onClick = onRestartTest,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Try Again",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Back to Tests",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ResultStat(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}