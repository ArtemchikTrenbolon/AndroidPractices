package com.example.androidpractices.ui.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Какие разрешения нужны для доступа к фото в зависимости от версии Android:
 * - API 34+: READ_MEDIA_IMAGES и READ_MEDIA_VISUAL_USER_SELECTED (пользователь может выдать доступ только к выбранным фото);
 * - API 33: READ_MEDIA_IMAGES;
 * - API 32 и ниже: READ_EXTERNAL_STORAGE.
 */
fun storagePermissions(): Array<String> = when {
    Build.VERSION.SDK_INT >= 34 -> arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
    )
    Build.VERSION.SDK_INT >= 33 -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}

/** Достаточно любого из разрешений набора (на Android 14 бывает только частичный доступ). */
fun Context.hasStorageAccess(): Boolean =
    storagePermissions().any {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

fun Context.hasCameraAccess(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
