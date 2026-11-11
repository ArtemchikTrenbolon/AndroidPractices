package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FilmRepository

class GetFilmUseCase(private val repository: FilmRepository) {
    suspend operator fun invoke(id: String): Result<Film> = repository.getFilm(id)
}
