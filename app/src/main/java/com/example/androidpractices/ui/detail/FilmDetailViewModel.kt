package com.example.androidpractices.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.data.FilmRepository
import com.example.androidpractices.model.Film
import com.example.androidpractices.ui.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FilmDetailUiState(
    val isLoading: Boolean = true,
    val film: Film? = null,
)

/** id фильма приходит из аргумента навигации через SavedStateHandle. */
class FilmDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val filmId: String = checkNotNull(savedStateHandle.get<String>(Routes.ARG_FILM_ID))

    private val _uiState = MutableStateFlow(FilmDetailUiState())
    val uiState: StateFlow<FilmDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val film = FilmRepository.getFilm(filmId)
            _uiState.value = FilmDetailUiState(isLoading = false, film = film)
        }
    }
}
