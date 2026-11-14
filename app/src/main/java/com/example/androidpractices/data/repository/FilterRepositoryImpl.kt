package com.example.androidpractices.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.androidpractices.domain.model.FilmFilter
import com.example.androidpractices.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FilterRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : FilterRepository {

    override val filter: Flow<FilmFilter> = dataStore.data
        .catch { e ->
            // Повреждённый файл настроек не должен ронять приложение: берём значения по умолчанию
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            FilmFilter(
                titleQuery = prefs[TITLE_QUERY] ?: "",
                minScore = prefs[MIN_SCORE] ?: 0,
                limit = prefs[LIMIT] ?: FilmFilter.DEFAULT_LIMIT,
            )
        }

    override suspend fun save(filter: FilmFilter) {
        dataStore.edit { prefs ->
            prefs[TITLE_QUERY] = filter.titleQuery
            prefs[MIN_SCORE] = filter.minScore
            prefs[LIMIT] = filter.limit
        }
    }

    private companion object {
        val TITLE_QUERY = stringPreferencesKey("title_query")
        val MIN_SCORE = intPreferencesKey("min_score")
        val LIMIT = intPreferencesKey("limit")
    }
}
