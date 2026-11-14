package com.example.androidpractices.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/** Отдельный файл настроек для профиля (фильтры лежат в своём файле). */
val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile")
