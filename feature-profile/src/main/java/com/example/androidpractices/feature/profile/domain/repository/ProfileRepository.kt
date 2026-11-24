package com.example.androidpractices.feature.profile.domain.repository

import com.example.androidpractices.feature.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    suspend fun save(profile: Profile)
}
