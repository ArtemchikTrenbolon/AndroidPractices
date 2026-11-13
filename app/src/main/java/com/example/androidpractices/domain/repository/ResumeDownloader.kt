package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.DownloadedFile

interface ResumeDownloader {
    suspend fun download(url: String): Result<DownloadedFile>
}
