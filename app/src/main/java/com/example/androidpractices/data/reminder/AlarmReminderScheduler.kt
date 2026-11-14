package com.example.androidpractices.data.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.androidpractices.domain.model.ClassTime
import com.example.androidpractices.domain.model.ReminderResult
import com.example.androidpractices.domain.repository.ReminderScheduler
import java.util.Calendar

class AlarmReminderScheduler(private val context: Context) : ReminderScheduler {

    private val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(time: ClassTime, userName: String): ReminderResult {
        val now = System.currentTimeMillis()
        val trigger = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // Дата срабатывания: сегодня. Если время уже прошло, переносим на завтра.
        val isNextDay = trigger.timeInMillis <= now
        if (isNextDay) trigger.add(Calendar.DAY_OF_YEAR, 1)

        // Одинаковый requestCode и FLAG_UPDATE_CURRENT: новое напоминание заменяет старое
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_USER_NAME, userName)
            putExtra(ReminderReceiver.EXTRA_TIME, time.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // С Android 12 для точных будильников нужно разрешение; если его нет, ставим неточный
        val canUseExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        if (canUseExact) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger.timeInMillis, pendingIntent)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger.timeInMillis, pendingIntent)
        }
        return ReminderResult(isNextDay = isNextDay, isExact = canUseExact)
    }

    private companion object {
        const val REQUEST_CODE = 2001
    }
}
