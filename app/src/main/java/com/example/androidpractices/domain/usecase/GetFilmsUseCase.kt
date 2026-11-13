package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.model.FilmFilter
import com.example.androidpractices.domain.repository.FilmRepository

class GetFilmsUseCase(private val repository: FilmRepository) {
    /** limit уходит на сервер, остальные условия фильтра применяются к ответу. */
    suspend operator fun invoke(filter: FilmFilter): Result<List<Film>> =
        repository.getFilms(filter.limit).map { films -> films.filter(filter::matches) }
}
