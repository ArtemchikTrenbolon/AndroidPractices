package com.example.androidpractices.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.DownloadedFile
import com.example.androidpractices.domain.model.Profile
import com.example.androidpractices.domain.repository.ProfileRepository
import com.example.androidpractices.domain.usecase.DownloadResumeUseCase
import com.example.androidpractices.ui.common.toUserMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Разовые события для экрана: открыть файл или показать сообщение. */
sealed interface ProfileEvent {
    data class OpenFile(val file: DownloadedFile) : ProfileEvent
    data class ShowMessage(val text: String) : ProfileEvent
}

class ProfileViewModel(
    profileRepository: ProfileRepository,
    private val downloadResumeUseCase: DownloadResumeUseCase,
) : ViewModel() {

    /** null, пока профиль читается из DataStore. */
    val profile: StateFlow<Profile?> = profileRepository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val _events = Channel<ProfileEvent>(Channel.BUFFERED)
    val events: Flow<ProfileEvent> = _events.receiveAsFlow()

    fun onResumeClick() {
        val url = profile.value?.resumeUrl?.takeIf { it.isNotBlank() } ?: return
        if (_isDownloading.value) return
        viewModelScope.launch {
            _isDownloading.value = true
            downloadResumeUseCase(url).fold(
                onSuccess = { _events.send(ProfileEvent.OpenFile(it)) },
                onFailure = { _events.send(ProfileEvent.ShowMessage(it.toUserMessage())) },
            )
            _isDownloading.value = false
        }
    }
}
