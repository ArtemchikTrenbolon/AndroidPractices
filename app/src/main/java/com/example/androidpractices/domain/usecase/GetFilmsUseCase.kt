package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FilmRepository

class GetFilmsUseCase(private val repository: FilmRepository) {
    suspend operator fun invoke(limit: Int): Result<List<Film>> = repository.getFilms(limit)
}
