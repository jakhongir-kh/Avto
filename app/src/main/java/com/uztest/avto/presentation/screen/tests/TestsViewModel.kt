package com.uztest.avto.presentation.screen.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uztest.avto.domain.model.Category
import com.uztest.avto.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TestsViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TestsUiState())
    val uiState: StateFlow<TestsUiState> = _uiState.asStateFlow()
    
    init {
        loadCategories()
    }
    
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val categories = getCategoriesUseCase()
                _uiState.value = _uiState.value.copy(
                    categories = categories,
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
}

data class TestsUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)