package com.uztest.avto.domain.model

data class TestResult(
    val categoryId: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeSpent: Long,
    val completedAt: Long = System.currentTimeMillis()
) {
    val percentage: Int get() = (correctAnswers * 100) / totalQuestions
    val isPassed: Boolean get() = percentage >= 80
}