package com.example.androidpractices.domain.model

/** Профиль пользователя. Хранится на устройстве. */
data class Profile(
    val fullName: String = "",
    val position: String = "",
    /** URI аватарки на устройстве (копия в приватной папке приложения). null, пока не выбрана. */
    val avatarUri: String? = null,
    /** Ссылка на резюме/портфолио в сети (например, pdf). */
    val resumeUrl: String = "",
)
