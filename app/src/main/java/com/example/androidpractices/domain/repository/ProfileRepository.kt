package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<Profile>
    suspend fun save(profile: Profile)
}
