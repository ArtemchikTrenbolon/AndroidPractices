package com.example.androidpractices.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.cache.FilterBadgeCache
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FilterRepository
import com.example.androidpractices.domain.usecase.GetFilmsUseCase
import com.example.androidpractices.ui.common.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface FilmsUiState {
    data object Loading : FilmsUiState
    data class Success(val films: List<Film>) : FilmsUiState
    data class Error(val message: String) : FilmsUiState
}

class FilmsViewModel(
    private val getFilmsUseCase: GetFilmsUseCase,
    filterRepository: FilterRepository,
    badgeCache: FilterBadgeCache,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmsUiState>(FilmsUiState.Loading)
    val uiState: StateFlow<FilmsUiState> = _uiState.asStateFlow()

    /** Нужно ли показывать бейдж на кнопке фильтров (общий кэш со экраном настроек). */
    val isFilterActive: StateFlow<Boolean> = badgeCache.isActive

    private val reloadTrigger = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            // Список перезагружается при каждом изменении сохранённых настроек и по кнопке «Повторить».
            // collectLatest отменяет устаревший запрос, если настройки сменились раньше ответа.
            combine(filterRepository.filter, reloadTrigger) { filter, _ -> filter }
                .collectLatest { filter ->
                    badgeCache.setActive(!filter.isDefault)
                    _uiState.value = FilmsUiState.Loading
                    getFilmsUseCase(filter).fold(
                        onSuccess = { _uiState.value = FilmsUiState.Success(it) },
                        onFailure = { _uiState.value = FilmsUiState.Error(it.toUserMessage()) },
                    )
                }
        }
    }

    fun retry() {
        reloadTrigger.update { it + 1 }
    }
}
