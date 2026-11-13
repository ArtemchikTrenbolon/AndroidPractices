package com.example.androidpractices.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Ответ Studio Ghibli API как есть. Числа сервер отдаёт строками ("1986", "124", "95"),
 * а Gson не проверяет null-безопасность Kotlin, поэтому необязательные поля помечены как nullable.
 */
data class FilmDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("original_title_romanised") val originalTitleRomanised: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("director") val director: String?,
    @SerializedName("producer") val producer: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("running_time") val runningTime: String?,
    @SerializedName("rt_score") val rtScore: String?,
)
