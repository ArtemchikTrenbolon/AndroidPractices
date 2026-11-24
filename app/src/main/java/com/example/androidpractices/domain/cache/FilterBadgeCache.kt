package com.example.androidpractices.domain.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Общий для экрана списка и экрана настроек кэш: нужно ли показывать бейдж «фильтры включены».
 * Живёт, пока живёт процесс, и нигде не сохраняется. Создаётся через DI (Koin) как single.
 */
class FilterBadgeCache {
    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    fun setActive(value: Boolean) {
        _isActive.value = value
    }
}
