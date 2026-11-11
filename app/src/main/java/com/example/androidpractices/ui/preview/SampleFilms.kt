package com.example.androidpractices.ui.preview

import com.example.androidpractices.domain.model.Film

/** Данные только для @Preview в Android Studio. */
object SampleFilms {
    val castleInTheSky = Film(
        id = "castle-in-the-sky",
        title = "Castle in the Sky",
        originalTitle = "天空の城ラピュタ",
        originalTitleRomanised = "Tenkū no shiro Rapyuta",
        description = "A young girl and a boy search for a legendary floating island.",
        director = "Hayao Miyazaki",
        producer = "Isao Takahata",
        releaseYear = 1986,
        runningTimeMinutes = 124,
        rtScore = 95,
        posterUrl = null,
    )
}
