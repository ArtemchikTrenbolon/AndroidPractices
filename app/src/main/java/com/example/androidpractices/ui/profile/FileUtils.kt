package com.example.androidpractices.ui.profile

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.androidpractices.domain.model.DownloadedFile
import java.io.File

private fun Context.fileProviderAuthority() = "$packageName.fileprovider"

/** Адрес для системной камеры: камера сама сохранит снимок в этот файл, разрешение CAMERA не нужно. */
fun Context.createCameraUri(): Uri {
    val dir = File(cacheDir, "camera").apply { mkdirs() }
    val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, fileProviderAuthority(), file)
}

/** Открывает скачанный файл во внешнем просмотрщике (для pdf это Drive, Chrome и т.п.). */
fun Context.openDownloadedFile(file: DownloadedFile) {
    val uri = FileProvider.getUriForFile(this, fileProviderAuthority(), File(file.path))
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, file.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Нет приложения для открытия этого файла", Toast.LENGTH_LONG).show()
    }
}
