package com.example.androidpractices.data.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Получает сигнал от AlarmManager в назначенное время и показывает уведомление. */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val userName = intent.getStringExtra(EXTRA_USER_NAME).orEmpty()
        val time = intent.getStringExtra(EXTRA_TIME).orEmpty()
        ReminderNotifier.show(context, userName, time)
    }

    companion object {
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_TIME = "time"
    }
}
