package com.example.androidpractices.di

import com.example.androidpractices.data.remote.FilmsApi
import com.example.androidpractices.data.repository.FilmRepositoryImpl
import com.example.androidpractices.domain.repository.FilmRepository
import com.example.androidpractices.domain.usecase.GetFilmUseCase
import com.example.androidpractices.domain.usecase.GetFilmsUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Ручная сборка зависимостей (вместо Hilt/Koin, которые появятся позже).
 * Всё создаётся лениво и один раз.
 */
object AppContainer {

    private const val BASE_URL = "https://ghibliapi.vercel.app/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            // Пишет запросы и ответы в Logcat (фильтр по тегу OkHttp)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    private val filmsApi: FilmsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmsApi::class.java)
    }

    private val filmRepository: FilmRepository by lazy { FilmRepositoryImpl(filmsApi) }

    val getFilmsUseCase: GetFilmsUseCase by lazy { GetFilmsUseCase(filmRepository) }
    val getFilmUseCase: GetFilmUseCase by lazy { GetFilmUseCase(filmRepository) }
}
