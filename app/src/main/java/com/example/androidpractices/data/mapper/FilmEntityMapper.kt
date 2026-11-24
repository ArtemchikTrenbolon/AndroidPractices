package com.example.androidpractices.data.mapper

import com.example.androidpractices.data.local.FilmEntity
import com.example.androidpractices.domain.model.Film

fun Film.toEntity(): FilmEntity = FilmEntity(
    id = id,
    title = title,
    originalTitle = originalTitle,
    originalTitleRomanised = originalTitleRomanised,
    description = description,
    director = director,
    producer = producer,
    releaseYear = releaseYear,
    runningTimeMinutes = runningTimeMinutes,
    rtScore = rtScore,
    posterUrl = posterUrl,
)

fun FilmEntity.toDomain(): Film = Film(
    id = id,
    title = title,
    originalTitle = originalTitle,
    originalTitleRomanised = originalTitleRomanised,
    description = description,
    director = director,
    producer = producer,
    releaseYear = releaseYear,
    runningTimeMinutes = runningTimeMinutes,
    rtScore = rtScore,
    posterUrl = posterUrl,
)
