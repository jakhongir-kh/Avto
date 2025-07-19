package com.uztest.avto.domain.repository

import com.uztest.avto.domain.model.Category
import com.uztest.avto.domain.model.Question

interface QuestionRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getQuestionsByCategory(categoryId: Int, limit: Int = 10): List<Question>
    suspend fun getAllQuestions(): List<Question>
}