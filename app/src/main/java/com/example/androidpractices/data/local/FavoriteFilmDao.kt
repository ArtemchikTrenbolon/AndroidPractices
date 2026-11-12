package com.example.androidpractices.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Query("SELECT * FROM favorite_films ORDER BY title")
    fun observeAll(): Flow<List<FilmEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE id = :id)")
    fun observeIsFavorite(id: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorite_films WHERE id = :id")
    suspend fun getById(id: String): FilmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FilmEntity)

    @Query("DELETE FROM favorite_films WHERE id = :id")
    suspend fun deleteById(id: String)
}
