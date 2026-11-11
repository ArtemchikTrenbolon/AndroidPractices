package com.example.androidpractices.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidpractices.di.AppContainer
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.usecase.GetFilmsUseCase
import com.example.androidpractices.ui.common.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface FilmsUiState {
    data object Loading : FilmsUiState
    data class Success(val films: List<Film>) : FilmsUiState
    data class Error(val message: String) : FilmsUiState
}

class FilmsViewModel(
    private val getFilmsUseCase: GetFilmsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmsUiState>(FilmsUiState.Loading)
    val uiState: StateFlow<FilmsUiState> = _uiState.asStateFlow()

    /** Сколько фильмов просим у сервера (query-параметр limit). */
    private val _limit = MutableStateFlow(DEFAULT_LIMIT)
    val limit: StateFlow<Int> = _limit.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun onLimitSelected(newLimit: Int) {
        if (newLimit == _limit.value && _uiState.value is FilmsUiState.Success) return
        _limit.value = newLimit
        load()
    }

    fun retry() = load()

    private fun load() {
        loadJob?.cancel() // если пользователь быстро меняет limit, старый запрос отменяем
        loadJob = viewModelScope.launch {
            _uiState.value = FilmsUiState.Loading
            getFilmsUseCase(_limit.value).fold(
                onSuccess = { _uiState.value = FilmsUiState.Success(it) },
                onFailure = { _uiState.value = FilmsUiState.Error(it.toUserMessage()) },
            )
        }
    }

    companion object {
        const val DEFAULT_LIMIT = 10

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { FilmsViewModel(AppContainer.getFilmsUseCase) }
        }
    }
}
