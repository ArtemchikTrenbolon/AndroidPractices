package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.ClassTime
import com.example.androidpractices.domain.model.ReminderResult

/** Ставит напоминание о паре. Реализация использует AlarmManager. */
interface ReminderScheduler {
    /** Заменяет ранее установленное напоминание. */
    fun schedule(time: ClassTime, userName: String): ReminderResult
}
