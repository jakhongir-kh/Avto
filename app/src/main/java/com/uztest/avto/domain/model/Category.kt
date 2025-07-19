package com.uztest.avto.domain.model

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val iconName: String,
    val totalQuestions: Int
)