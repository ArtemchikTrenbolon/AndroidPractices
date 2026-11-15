package com.example.androidpractices.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.data.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FilmsUiState(
    val isLoading: Boolean = true,
    val films: List<FilmListItemUi> = emptyList(),
)

class FilmsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FilmsUiState())
    val uiState: StateFlow<FilmsUiState> = _uiState.asStateFlow()

    init {
        loadFilms()
    }

    private fun loadFilms() {
        viewModelScope.launch {
            // Форматирование для экрана делает ViewModel: в UI уходят готовые строки
            val films = FilmRepository.getFilms().map { it.toListItemUi() }
            _uiState.value = FilmsUiState(isLoading = false, films = films)
        }
    }
}
