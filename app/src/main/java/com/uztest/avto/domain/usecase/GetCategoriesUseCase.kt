package com.uztest.avto.domain.usecase

import com.uztest.avto.domain.model.Category
import com.uztest.avto.domain.repository.QuestionRepository

class GetCategoriesUseCase(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}