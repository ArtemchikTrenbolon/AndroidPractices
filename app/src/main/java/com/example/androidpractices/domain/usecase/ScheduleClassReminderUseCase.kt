package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.ClassTime
import com.example.androidpractices.domain.model.ReminderResult
import com.example.androidpractices.domain.repository.ReminderScheduler

class ScheduleClassReminderUseCase(private val scheduler: ReminderScheduler) {
    operator fun invoke(time: ClassTime, userName: String): ReminderResult =
        scheduler.schedule(time, userName.trim().ifEmpty { "Студент" })
}
