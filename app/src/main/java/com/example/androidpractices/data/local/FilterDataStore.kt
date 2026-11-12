package com.example.androidpractices.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/** Файл настроек фильтра. Один экземпляр на приложение (так требует DataStore). */
val Context.filterDataStore: DataStore<Preferences> by preferencesDataStore(name = "film_filter")
