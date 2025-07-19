package com.uztest.avto.domain.usecase

import com.uztest.avto.domain.model.Question
import com.uztest.avto.domain.repository.QuestionRepository
import javax.inject.Inject

class GetTestQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(categoryId: Int): List<Question> {
        return repository.getQuestionsByCategory(categoryId, 10)
    }
}