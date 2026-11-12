package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.FilmFilter
import kotlinx.coroutines.flow.Flow

/** Сохраняемые между запусками настройки фильтра. */
interface FilterRepository {
    val filter: Flow<FilmFilter>
    suspend fun save(filter: FilmFilter)
}
