package com.example.androidpractices.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FavoritesRepository
import com.example.androidpractices.domain.usecase.GetFilmUseCase
import com.example.androidpractices.domain.usecase.ToggleFavoriteUseCase
import com.example.androidpractices.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface FilmDetailUiState {
    data object Loading : FilmDetailUiState
    data class Success(val film: Film) : FilmDetailUiState
    data class Error(val message: String) : FilmDetailUiState
}

/** id фильма (path-параметр запроса) передаёт Koin через parametersOf. */
class FilmDetailViewModel(
    private val filmId: String,
    private val getFilmUseCase: GetFilmUseCase,
    favoritesRepository: FavoritesRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmDetailUiState>(FilmDetailUiState.Loading)
    val uiState: StateFlow<FilmDetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favoritesRepository.observeIsFavorite(filmId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        load()
    }

    fun retry() = load()

    fun toggleFavorite() {
        val film = (_uiState.value as? FilmDetailUiState.Success)?.film ?: return
        viewModelScope.launch { toggleFavoriteUseCase(film) }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = FilmDetailUiState.Loading
            getFilmUseCase(filmId).fold(
                onSuccess = { _uiState.value = FilmDetailUiState.Success(it) },
                onFailure = { _uiState.value = FilmDetailUiState.Error(it.toUserMessage()) },
            )
        }
    }
}
