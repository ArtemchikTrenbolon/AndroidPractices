package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.DownloadedFile
import com.example.androidpractices.domain.repository.ResumeDownloader

class DownloadResumeUseCase(private val downloader: ResumeDownloader) {
    suspend operator fun invoke(url: String): Result<DownloadedFile> = downloader.download(url.trim())
}
