package com.example.androidpractices.domain.model

/** Доменная модель фильма: не знает ни про сеть, ни про Android. */
data class Film(
    val id: String,
    val title: String,
    val originalTitle: String,
    val originalTitleRomanised: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseYear: Int,
    val runningTimeMinutes: Int,
    /** Оценка на Rotten Tomatoes, 0..100 */
    val rtScore: Int,
    val posterUrl: String?,
)
