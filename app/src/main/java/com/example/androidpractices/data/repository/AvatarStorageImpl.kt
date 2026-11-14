package com.example.androidpractices.data.repository

import android.content.Context
import android.net.Uri
import com.example.androidpractices.domain.model.AppException
import com.example.androidpractices.domain.repository.AvatarStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.CancellationException

/**
 * Копирует выбранное фото в приватную папку приложения. Это нужно, потому что доступ к URI
 * из галереи временный и пропадёт после перезапуска. Имя файла с меткой времени, чтобы
 * новая аватарка получила новый URI и интерфейс не показывал старую картинку из кэша.
 */
class AvatarStorageImpl(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AvatarStorage {

    override suspend fun saveAvatar(sourceUri: String): Result<String> = withContext(ioDispatcher) {
        try {
            val target = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
            val input = context.contentResolver.openInputStream(Uri.parse(sourceUri))
                ?: throw IOException("Не удалось открыть $sourceUri")
            input.use { src -> target.outputStream().use { dst -> src.copyTo(dst) } }
            Result.success(Uri.fromFile(target).toString())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(AppException.Unknown(e))
        }
    }
}
