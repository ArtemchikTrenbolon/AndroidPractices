package com.example.androidpractices.data.mapper

import com.example.androidpractices.data.remote.FilmDto
import com.example.androidpractices.domain.model.Film

fun FilmDto.toDomain(): Film = Film(
    id = id,
    title = title,
    originalTitle = originalTitle.orEmpty(),
    originalTitleRomanised = originalTitleRomanised.orEmpty(),
    description = description.orEmpty(),
    director = director.orEmpty(),
    producer = producer.orEmpty(),
    releaseYear = releaseDate?.toIntOrNull() ?: 0,
    runningTimeMinutes = runningTime?.toIntOrNull() ?: 0,
    rtScore = rtScore?.toIntOrNull() ?: 0,
    posterUrl = image?.takeIf { it.isNotBlank() },
)
