package com.example.androidpractices.feature.profile.domain.usecase

import com.example.androidpractices.feature.profile.domain.model.Profile
import com.example.androidpractices.feature.profile.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: Profile) = repository.save(
        profile.copy(
            fullName = profile.fullName.trim(),
            position = profile.position.trim(),
            resumeUrl = profile.resumeUrl.trim(),
            classTime = profile.classTime.trim(),
        )
    )
}
