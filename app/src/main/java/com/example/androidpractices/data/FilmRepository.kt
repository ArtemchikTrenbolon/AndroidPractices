package com.example.androidpractices.data

import com.example.androidpractices.model.Film
import kotlinx.coroutines.delay

/**
 * Источник данных. Функции suspend и с небольшой задержкой, как будто это сеть:
 * так ViewModel и экраны уже умеют показывать загрузку. В практике 4 тело функций
 * заменится на запрос к API, а остальной код менять не придётся.
 */
object FilmRepository {

    suspend fun getFilms(): List<Film> {
        delay(600)
        return MockFilms.films
    }

    suspend fun getFilm(id: String): Film? {
        delay(300)
        return MockFilms.films.firstOrNull { it.id == id }
    }
}
