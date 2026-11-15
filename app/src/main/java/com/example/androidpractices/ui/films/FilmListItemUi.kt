package com.example.androidpractices.ui.films

import com.example.androidpractices.model.Film

/** Готовая к показу карточка фильма в списке: экран только выводит строки, ничего не собирает. */
data class FilmListItemUi(
    val id: String,
    val title: String,
    val romanisedTitle: String,
    /** Например: «1986 • Hayao Miyazaki» */
    val subtitle: String,
    /** Например: «★ 95» */
    val scoreText: String,
    /** Буква для заглушки постера */
    val posterInitial: String,
)

fun Film.toListItemUi(): FilmListItemUi = FilmListItemUi(
    id = id,
    title = title,
    romanisedTitle = originalTitleRomanised,
    subtitle = "$releaseYear • $director",
    scoreText = "★ $rtScore",
    posterInitial = title.take(1).uppercase(),
)
