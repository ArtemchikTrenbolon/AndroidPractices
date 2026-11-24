package com.example.androidpractices.feature.profile.domain.repository

import com.example.androidpractices.feature.profile.domain.model.ClassTime
import com.example.androidpractices.feature.profile.domain.model.ReminderResult

/** Ставит напоминание о паре. Реализация использует AlarmManager. */
interface ReminderScheduler {
    /** Заменяет ранее установленное напоминание. */
    fun schedule(time: ClassTime, userName: String): ReminderResult
}
