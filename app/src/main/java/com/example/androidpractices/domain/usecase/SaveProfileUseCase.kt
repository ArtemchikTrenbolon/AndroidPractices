package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Profile
import com.example.androidpractices.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = repository.save(
        profile.copy(
            fullName = profile.fullName.trim(),
            position = profile.position.trim(),
            resumeUrl = profile.resumeUrl.trim(),
        )
    )
}
