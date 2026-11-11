package com.example.androidpractices.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidpractices.di.AppContainer
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.usecase.GetFilmUseCase
import com.example.androidpractices.ui.common.toUserMessage
import com.example.androidpractices.ui.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FilmDetailUiState {
    data object Loading : FilmDetailUiState
    data class Success(val film: Film) : FilmDetailUiState
    data class Error(val message: String) : FilmDetailUiState
}

/** id фильма (path-параметр запроса) приходит из аргумента навигации через SavedStateHandle. */
class FilmDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getFilmUseCase: GetFilmUseCase,
) : ViewModel() {

    private val filmId: String = checkNotNull(savedStateHandle.get<String>(Routes.ARG_FILM_ID))

    private val _uiState = MutableStateFlow<FilmDetailUiState>(FilmDetailUiState.Loading)
    val uiState: StateFlow<FilmDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _uiState.value = FilmDetailUiState.Loading
            getFilmUseCase(filmId).fold(
                onSuccess = { _uiState.value = FilmDetailUiState.Success(it) },
                onFailure = { _uiState.value = FilmDetailUiState.Error(it.toUserMessage()) },
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FilmDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    getFilmUseCase = AppContainer.getFilmUseCase,
                )
            }
        }
    }
}
