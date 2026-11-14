package com.example.androidpractices.data.repository

import com.example.androidpractices.data.local.FavoriteFilmDao
import com.example.androidpractices.data.mapper.toDomain
import com.example.androidpractices.data.mapper.toEntity
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val dao: FavoriteFilmDao) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<Film>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeIsFavorite(id: String): Flow<Boolean> = dao.observeIsFavorite(id)

    override suspend fun isFavorite(id: String): Boolean = dao.isFavorite(id)

    override suspend fun getFavorite(id: String): Film? = dao.getById(id)?.toDomain()

    override suspend fun add(film: Film) = dao.insert(film.toEntity())

    override suspend fun remove(id: String) = dao.deleteById(id)
}
