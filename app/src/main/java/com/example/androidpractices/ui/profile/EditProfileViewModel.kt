package com.example.androidpractices.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Profile
import com.example.androidpractices.domain.repository.ProfileRepository
import com.example.androidpractices.domain.usecase.SaveAvatarUseCase
import com.example.androidpractices.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    profileRepository: ProfileRepository,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val saveAvatarUseCase: SaveAvatarUseCase,
) : ViewModel() {

    /** Редактируемая копия профиля. null, пока не прочитали сохранённый. */
    private val _draft = MutableStateFlow<Profile?>(null)
    val draft: StateFlow<Profile?> = _draft.asStateFlow()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    init {
        viewModelScope.launch { _draft.value = profileRepository.profile.first() }
    }

    fun onNameChange(value: String) = _draft.update { it?.copy(fullName = value) }

    fun onPositionChange(value: String) = _draft.update { it?.copy(position = value) }

    fun onResumeUrlChange(value: String) = _draft.update { it?.copy(resumeUrl = value) }

    /** Фото выбрано в галерее или снято камерой: копируем в память приложения и показываем. */
    fun onAvatarPicked(sourceUri: String) {
        viewModelScope.launch {
            saveAvatarUseCase(sourceUri).fold(
                onSuccess = { storedUri -> _draft.update { it?.copy(avatarUri = storedUri) } },
                onFailure = { _messages.send("Не удалось сохранить фото") },
            )
        }
    }

    /** «Готово»: сохраняем профиль и закрываем экран. Экран профиля обновится сам (Flow из DataStore). */
    fun save(onSaved: () -> Unit) {
        val profile = _draft.value ?: return
        viewModelScope.launch {
            saveProfileUseCase(profile)
            onSaved()
        }
    }
}
