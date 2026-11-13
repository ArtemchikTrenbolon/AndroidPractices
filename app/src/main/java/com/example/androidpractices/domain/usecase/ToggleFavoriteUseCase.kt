package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(film: Film) {
        if (repository.isFavorite(film.id)) repository.remove(film.id) else repository.add(film)
    }
}
