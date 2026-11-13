package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.repository.AvatarStorage

class SaveAvatarUseCase(private val storage: AvatarStorage) {
    suspend operator fun invoke(sourceUri: String): Result<String> = storage.saveAvatar(sourceUri)
}
