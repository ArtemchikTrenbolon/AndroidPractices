package com.example.androidpractices.data.repository

import android.content.Context
import android.webkit.MimeTypeMap
import com.example.androidpractices.domain.model.AppException
import com.example.androidpractices.domain.model.DownloadedFile
import com.example.androidpractices.domain.repository.ResumeDownloader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.util.concurrent.CancellationException

/** Скачивает файл по ссылке в кэш приложения (папка resumes). */
class ResumeDownloaderImpl(
    private val context: Context,
    private val client: OkHttpClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ResumeDownloader {

    override suspend fun download(url: String): Result<DownloadedFile> = withContext(ioDispatcher) {
        val httpUrl = url.toHttpUrlOrNull() ?: return@withContext Result.failure(AppException.InvalidUrl())
        try {
            client.newCall(Request.Builder().url(httpUrl).build()).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        if (response.code == 404) AppException.NotFound() else AppException.Server(response.code)
                    )
                }
                val body = response.body ?: return@withContext Result.failure(AppException.Unknown())

                val fileName = httpUrl.pathSegments.lastOrNull()
                    ?.takeIf { it.isNotBlank() }
                    ?.replace(Regex("[^A-Za-z0-9._-]"), "_")
                    ?: "resume"
                val dir = File(context.cacheDir, "resumes").apply { mkdirs() }
                val file = File(dir, fileName)
                body.byteStream().use { input -> file.outputStream().use { output -> input.copyTo(output) } }

                val mime = body.contentType()?.let { "${it.type}/${it.subtype}" }
                    ?.takeUnless { it == "application/octet-stream" }
                    ?: MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
                    ?: "*/*"
                Result.success(DownloadedFile(path = file.absolutePath, mimeType = mime))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(AppException.NoInternet(e))
        } catch (e: Exception) {
            Result.failure(AppException.Unknown(e))
        }
    }
}
