package com.example.androidpractices.ui.detail

import com.example.androidpractices.model.Film

/** Готовая к показу информация о фильме: все строки собраны заранее, экран ничего не форматирует. */
data class FilmDetailUi(
    val id: String,
    val title: String,
    val originalTitle: String,
    val romanisedTitle: String,
    val description: String,
    val director: String,
    val producer: String,
    /** Например: «★ 95 / 100» */
    val scoreText: String,
    /** Например: «1986» */
    val yearText: String,
    /** Например: «124 мин» */
    val durationText: String,
    /** Буква для заглушки постера */
    val posterInitial: String,
)

fun Film.toDetailUi(): FilmDetailUi = FilmDetailUi(
    id = id,
    title = title,
    originalTitle = originalTitle,
    romanisedTitle = originalTitleRomanised,
    description = description,
    director = director,
    producer = producer,
    scoreText = "★ $rtScore / 100",
    yearText = releaseYear.toString(),
    durationText = "$runningTimeMinutes мин",
    posterInitial = title.take(1).uppercase(),
)
