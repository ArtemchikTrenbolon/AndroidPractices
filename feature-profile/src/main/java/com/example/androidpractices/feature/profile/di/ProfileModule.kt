package com.example.androidpractices.feature.profile.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.androidpractices.feature.profile.data.local.profileDataStore
import com.example.androidpractices.feature.profile.data.reminder.AlarmReminderScheduler
import com.example.androidpractices.feature.profile.data.repository.AvatarStorageImpl
import com.example.androidpractices.feature.profile.data.repository.ProfileRepositoryImpl
import com.example.androidpractices.feature.profile.data.repository.ResumeDownloaderImpl
import com.example.androidpractices.feature.profile.domain.repository.AvatarStorage
import com.example.androidpractices.feature.profile.domain.repository.ProfileRepository
import com.example.androidpractices.feature.profile.domain.repository.ReminderScheduler
import com.example.androidpractices.feature.profile.domain.repository.ResumeDownloader
import com.example.androidpractices.feature.profile.domain.usecase.DownloadResumeUseCase
import com.example.androidpractices.feature.profile.domain.usecase.SaveAvatarUseCase
import com.example.androidpractices.feature.profile.domain.usecase.SaveProfileUseCase
import com.example.androidpractices.feature.profile.domain.usecase.ScheduleClassReminderUseCase
import com.example.androidpractices.feature.profile.ui.EditProfileViewModel
import com.example.androidpractices.feature.profile.ui.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Зависимости фичи «Профиль». Приложение подключает этот модуль в GhibliApp. */
val profileModule = module {

    // Данные
    single<DataStore<Preferences>>(named("profile")) { androidContext().profileDataStore }
    single<ProfileRepository> { ProfileRepositoryImpl(get(named("profile"))) }
    single<AvatarStorage> { AvatarStorageImpl(androidContext()) }
    single<ResumeDownloader> { ResumeDownloaderImpl(androidContext()) }
    single<ReminderScheduler> { AlarmReminderScheduler(androidContext()) }

    // Use case
    factory { SaveProfileUseCase(get()) }
    factory { SaveAvatarUseCase(get()) }
    factory { DownloadResumeUseCase(get()) }
    factory { ScheduleClassReminderUseCase(get()) }

    // ViewModel
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get(), get(), get()) }
}
