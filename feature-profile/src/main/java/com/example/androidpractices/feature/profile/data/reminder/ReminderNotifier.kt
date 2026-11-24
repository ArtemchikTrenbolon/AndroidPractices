package com.example.androidpractices.feature.profile.data.reminder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

/** Канал и показ уведомления о начале пары. */
object ReminderNotifier {

    private const val CHANNEL_ID = "class_reminder"
    private const val NOTIFICATION_ID = 1001

    /** С Android 8 уведомление без канала не показывается. Создание канала можно повторять. */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Напоминания о паре",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply { description = "Уведомление о начале любимой пары" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun show(context: Context, userName: String, time: String) {
        ensureChannel(context)

        // С Android 13 без разрешения POST_NOTIFICATIONS показывать уведомления нельзя
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        // По нажатию открываем приложение. Стартовый экран берём из системы по имени пакета,
        // поэтому модулю не нужен класс MainActivity из app.
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP) }
            ?: return
        val openApp = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Время любимой пары")
            .setContentText("$userName, ваша любимая пара начинается ($time)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(openApp)
            .build()

        context.getSystemService(NotificationManager::class.java).notify(NOTIFICATION_ID, notification)
    }
}
