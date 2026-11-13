package com.example.androidpractices.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.androidpractices.data.local.AppDatabase
import com.example.androidpractices.data.local.filterDataStore
import com.example.androidpractices.data.local.profileDataStore
import com.example.androidpractices.data.remote.FilmsApi
import com.example.androidpractices.data.repository.FavoritesRepositoryImpl
import com.example.androidpractices.data.repository.FilmRepositoryImpl
import com.example.androidpractices.data.repository.AvatarStorageImpl
import com.example.androidpractices.data.repository.FilterRepositoryImpl
import com.example.androidpractices.data.repository.ProfileRepositoryImpl
import com.example.androidpractices.data.repository.ResumeDownloaderImpl
import com.example.androidpractices.domain.cache.FilterBadgeCache
import com.example.androidpractices.domain.repository.FavoritesRepository
import com.example.androidpractices.domain.repository.FilmRepository
import com.example.androidpractices.domain.repository.AvatarStorage
import com.example.androidpractices.domain.repository.FilterRepository
import com.example.androidpractices.domain.repository.ProfileRepository
import com.example.androidpractices.domain.repository.ResumeDownloader
import com.example.androidpractices.domain.usecase.DownloadResumeUseCase
import com.example.androidpractices.domain.usecase.GetFilmUseCase
import com.example.androidpractices.domain.usecase.GetFilmsUseCase
import com.example.androidpractices.domain.usecase.SaveAvatarUseCase
import com.example.androidpractices.domain.usecase.SaveProfileUseCase
import com.example.androidpractices.domain.usecase.ToggleFavoriteUseCase
import com.example.androidpractices.ui.detail.FilmDetailViewModel
import com.example.androidpractices.ui.favorites.FavoritesViewModel
import com.example.androidpractices.ui.profile.EditProfileViewModel
import com.example.androidpractices.ui.profile.ProfileViewModel
import com.example.androidpractices.ui.films.FilmsViewModel
import com.example.androidpractices.ui.settings.FilterSettingsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://ghibliapi.vercel.app/"

/** Все зависимости приложения (DI на Koin). single = один экземпляр, factory = новый каждый раз. */
val appModule = module {

    // Сеть
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }
    single<FilmsApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmsApi::class.java)
    }
    single<FilmRepository> { FilmRepositoryImpl(get()) }

    // Локальное хранение
    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "ghibli.db").build()
    }
    single { get<AppDatabase>().favoriteFilmDao() }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<DataStore<Preferences>>(named("filter")) { androidContext().filterDataStore }
    single<FilterRepository> { FilterRepositoryImpl(get(named("filter"))) }

    // Профиль: настройки в DataStore, аватарка и резюме в файлах приложения
    single<DataStore<Preferences>>(named("profile")) { androidContext().profileDataStore }
    single<ProfileRepository> { ProfileRepositoryImpl(get(named("profile"))) }
    single<AvatarStorage> { AvatarStorageImpl(androidContext()) }
    single<ResumeDownloader> { ResumeDownloaderImpl(androidContext(), get()) }

    // Общий кэш бейджа: один экземпляр для списка и настроек
    single { FilterBadgeCache() }

    // Use case
    factory { GetFilmsUseCase(get()) }
    factory { GetFilmUseCase(get(), get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { SaveProfileUseCase(get()) }
    factory { SaveAvatarUseCase(get()) }
    factory { DownloadResumeUseCase(get()) }

    // ViewModel
    viewModel { FilmsViewModel(get(), get(), get()) }
    viewModel { FilterSettingsViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get()) }
    viewModel { params -> FilmDetailViewModel(params.get(), get(), get(), get()) }
}
