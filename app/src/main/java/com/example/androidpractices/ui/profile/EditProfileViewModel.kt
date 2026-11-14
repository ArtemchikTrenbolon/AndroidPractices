package com.example.androidpractices.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.ClassTime
import com.example.androidpractices.domain.model.Profile
import com.example.androidpractices.domain.model.ReminderResult
import com.example.androidpractices.domain.repository.ProfileRepository
import com.example.androidpractices.domain.usecase.SaveAvatarUseCase
import com.example.androidpractices.domain.usecase.SaveProfileUseCase
import com.example.androidpractices.domain.usecase.ScheduleClassReminderUseCase
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
    private val scheduleClassReminderUseCase: ScheduleClassReminderUseCase,
) : ViewModel() {

    /** Редактируемая копия профиля. null, пока не прочитали сохранённый. */
    private val _draft = MutableStateFlow<Profile?>(null)
    val draft: StateFlow<Profile?> = _draft.asStateFlow()

    /** Ошибку формата времени показываем только после того, как пользователь начал вводить. */
    private val _classTimeTouched = MutableStateFlow(false)
    val classTimeTouched: StateFlow<Boolean> = _classTimeTouched.asStateFlow()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    init {
        viewModelScope.launch { _draft.value = profileRepository.profile.first() }
    }

    fun onNameChange(value: String) = _draft.update { it?.copy(fullName = value) }

    fun onPositionChange(value: String) = _draft.update { it?.copy(position = value) }

    fun onResumeUrlChange(value: String) = _draft.update { it?.copy(resumeUrl = value) }

    /** Время меняется и с клавиатуры, и из окна с часами. */
    fun onClassTimeChange(value: String) {
        _classTimeTouched.value = true
        _draft.update { it?.copy(classTime = value) }
    }

    /** Фото выбрано в галерее или снято камерой: копируем в память приложения и показываем. */
    fun onAvatarPicked(sourceUri: String) {
        viewModelScope.launch {
            saveAvatarUseCase(sourceUri).fold(
                onSuccess = { storedUri -> _draft.update { it?.copy(avatarUri = storedUri) } },
                onFailure = { _messages.send("Не удалось сохранить фото") },
            )
        }
    }

    /**
     * «Готово»: сохраняем профиль, ставим напоминание и закрываем экран.
     * Если время некорректно, ничего не делаем (кнопка в этом случае и так отключена).
     * В onSaved передаём текст для пользователя про установленное напоминание.
     */
    fun save(onSaved: (String?) -> Unit) {
        val profile = _draft.value ?: return
        val classTime = ClassTime.parse(profile.classTime) ?: return
        viewModelScope.launch {
            saveProfileUseCase(profile)
            val result = scheduleClassReminderUseCase(classTime, profile.fullName)
            onSaved(result.toMessage(classTime))
        }
    }

    private fun ReminderResult.toMessage(time: ClassTime): String {
        val day = if (isNextDay) "завтра" else "сегодня"
        val base = "Напоминание установлено на $time ($day)"
        return if (isExact) base else "$base. Без разрешения на точные будильники оно может прийти с задержкой"
    }
}
