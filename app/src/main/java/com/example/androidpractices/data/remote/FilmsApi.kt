package com.example.androidpractices.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApi {

    /** GET /films?limit=N — query-параметр. */
    @GET("films")
    suspend fun getFilms(@Query("limit") limit: Int): List<FilmDto>

    /** GET /films/{id} — path-параметр. */
    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: String): FilmDto
}
