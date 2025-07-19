package com.uztest.avto.domain.model

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val category: Category,
    val explanation: String = ""
)