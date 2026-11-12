package com.example.androidpractices.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Фильм в избранном: сохраняем целиком, чтобы открыть его без интернета. */
@Entity(tableName = "favorite_films")
data class FilmEntity(
    @PrimaryKey val id: String,
    val title: String,
    val originalTitle: String,
    val originalTitleRomanised: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseYear: Int,
    val runningTimeMinutes: Int,
    val rtScore: Int,
    val posterUrl: String?,
)
