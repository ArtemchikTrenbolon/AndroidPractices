package com.example.androidpractices.feature.profile.domain.repository

import com.example.androidpractices.feature.profile.domain.model.DownloadedFile

interface ResumeDownloader {
    suspend fun download(url: String): Result<DownloadedFile>
}
