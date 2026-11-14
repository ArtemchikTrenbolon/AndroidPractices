package com.example.androidpractices.domain.repository

/** Сохраняет выбранное фото в память приложения. */
interface AvatarStorage {
    /** @param sourceUri URI из галереи или камеры; @return URI постоянной копии в приватной папке */
    suspend fun saveAvatar(sourceUri: String): Result<String>
}
