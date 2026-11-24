package com.example.androidpractices.feature.profile.domain.usecase

import com.example.androidpractices.feature.profile.domain.model.DownloadedFile
import com.example.androidpractices.feature.profile.domain.repository.ResumeDownloader

class DownloadResumeUseCase(private val downloader: ResumeDownloader) {
    suspend operator fun invoke(url: String): Result<DownloadedFile> = downloader.download(url.trim())
}
