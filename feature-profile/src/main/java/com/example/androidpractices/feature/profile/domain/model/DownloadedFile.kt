package com.example.androidpractices.feature.profile.domain.model

/** Скачанный файл: путь на диске и MIME-тип, чтобы выбрать подходящий просмотрщик. */
data class DownloadedFile(
    val path: String,
    val mimeType: String,
)
