package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.Film
import kotlinx.coroutines.flow.Flow

/** Избранное: хранится локально и доступно без интернета. */
interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Film>>
    fun observeIsFavorite(id: String): Flow<Boolean>
    suspend fun isFavorite(id: String): Boolean
    suspend fun getFavorite(id: String): Film?
    suspend fun add(film: Film)
    suspend fun remove(id: String)
}
