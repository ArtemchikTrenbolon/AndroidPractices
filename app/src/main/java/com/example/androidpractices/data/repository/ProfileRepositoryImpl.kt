package com.example.androidpractices.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.androidpractices.domain.model.Profile
import com.example.androidpractices.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : ProfileRepository {

    override val profile: Flow<Profile> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            Profile(
                fullName = prefs[FULL_NAME].orEmpty(),
                position = prefs[POSITION].orEmpty(),
                avatarUri = prefs[AVATAR_URI],
                resumeUrl = prefs[RESUME_URL].orEmpty(),
                classTime = prefs[CLASS_TIME].orEmpty(),
            )
        }

    override suspend fun save(profile: Profile) {
        dataStore.edit { prefs ->
            prefs[FULL_NAME] = profile.fullName
            prefs[POSITION] = profile.position
            prefs[RESUME_URL] = profile.resumeUrl
            prefs[CLASS_TIME] = profile.classTime
            val avatar = profile.avatarUri
            if (avatar == null) prefs.remove(AVATAR_URI) else prefs[AVATAR_URI] = avatar
        }
    }

    private companion object {
        val FULL_NAME = stringPreferencesKey("full_name")
        val POSITION = stringPreferencesKey("position")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
        val CLASS_TIME = stringPreferencesKey("class_time")
    }
}
