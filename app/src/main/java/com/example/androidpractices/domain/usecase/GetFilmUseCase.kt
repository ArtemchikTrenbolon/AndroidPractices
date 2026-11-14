package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FavoritesRepository
import com.example.androidpractices.domain.repository.FilmRepository

class GetFilmUseCase(
    private val filmRepository: FilmRepository,
    private val favoritesRepository: FavoritesRepository,
) {
    /** Сначала сеть; если её нет, а фильм в избранном, берём сохранённую копию (работает офлайн). */
    suspend operator fun invoke(id: String): Result<Film> {
        val remote = filmRepository.getFilm(id)
        if (remote.isSuccess) return remote
        val cached = favoritesRepository.getFavorite(id)
        return if (cached != null) Result.success(cached) else remote
    }
}
