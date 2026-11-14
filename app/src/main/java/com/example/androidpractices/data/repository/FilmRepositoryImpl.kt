package com.example.androidpractices.data.repository

import com.example.androidpractices.data.mapper.toDomain
import com.example.androidpractices.data.remote.FilmsApi
import com.example.androidpractices.domain.model.AppException
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.domain.repository.FilmRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.CancellationException

class FilmRepositoryImpl(
    private val api: FilmsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FilmRepository {

    override suspend fun getFilms(limit: Int): Result<List<Film>> =
        safeApiCall { api.getFilms(limit).map { it.toDomain() } }

    override suspend fun getFilm(id: String): Result<Film> =
        safeApiCall { api.getFilm(id).toDomain() }

    /**
     * Выполняет запрос на IO-диспатчере и переводит сетевые исключения в AppException.
     * CancellationException пробрасываем дальше, иначе сломается отмена корутин.
     */
    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> =
        withContext(ioDispatcher) {
            try {
                Result.success(block())
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                Result.failure(AppException.NoInternet(e))
            } catch (e: HttpException) {
                Result.failure(
                    if (e.code() == 404) AppException.NotFound() else AppException.Server(e.code())
                )
            } catch (e: Exception) {
                Result.failure(AppException.Unknown(e))
            }
        }
}
