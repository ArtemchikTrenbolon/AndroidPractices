package com.example.androidpractices.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.cache.FilterBadgeCache
import com.example.androidpractices.domain.model.FilmFilter
import com.example.androidpractices.domain.repository.FilterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilterSettingsViewModel(
    private val filterRepository: FilterRepository,
    private val badgeCache: FilterBadgeCache,
) : ViewModel() {

    /** Редактируемая копия настроек. null, пока не прочитали сохранённые значения. */
    private val _draft = MutableStateFlow<FilmFilter?>(null)
    val draft: StateFlow<FilmFilter?> = _draft.asStateFlow()

    init {
        viewModelScope.launch { _draft.value = filterRepository.filter.first() }
    }

    fun onTitleChange(value: String) = _draft.update { it?.copy(titleQuery = value) }

    fun onMinScoreChange(value: Int) = _draft.update { it?.copy(minScore = value) }

    fun onLimitChange(value: Int) = _draft.update { it?.copy(limit = value) }

    fun reset() {
        _draft.value = FilmFilter()
    }

    /** «Готово»: сохраняем в DataStore, обновляем бейдж и закрываем экран. Список перезагрузится сам. */
    fun save(onSaved: () -> Unit) {
        val filter = _draft.value?.let { it.copy(titleQuery = it.titleQuery.trim()) } ?: return
        viewModelScope.launch {
            filterRepository.save(filter)
            badgeCache.setActive(!filter.isDefault)
            onSaved()
        }
    }
}
