package com.example.androidpractices.model

/**
 * Фильм студии Ghibli. Поля повторяют ответ Studio Ghibli API,
 * чтобы в следующих практиках модель почти не менялась.
 */
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
    /** Пока не используется: в этой практике сеть не нужна. */
    val posterUrl: String? = null,
)
