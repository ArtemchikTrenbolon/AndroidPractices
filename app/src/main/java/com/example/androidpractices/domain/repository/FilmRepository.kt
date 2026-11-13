package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.Film

/** Контракт доступа к данным. Реализация живёт в слое data. */
interface FilmRepository {
    /** @param limit сколько фильмов запросить (query-параметр limit) */
    suspend fun getFilms(limit: Int): Result<List<Film>>

    /** @param id идентификатор фильма (path-параметр) */
    suspend fun getFilm(id: String): Result<Film>
}
