package com.example.androidpractices.feature.profile.domain.usecase

import com.example.androidpractices.feature.profile.domain.repository.AvatarStorage

class SaveAvatarUseCase(private val storage: AvatarStorage) {
    suspend operator fun invoke(sourceUri: String): Result<String> = storage.saveAvatar(sourceUri)
}
