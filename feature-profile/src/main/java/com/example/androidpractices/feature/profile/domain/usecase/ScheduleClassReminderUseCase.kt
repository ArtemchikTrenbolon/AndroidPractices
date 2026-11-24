package com.example.androidpractices.feature.profile.domain.usecase

import com.example.androidpractices.feature.profile.domain.model.ClassTime
import com.example.androidpractices.feature.profile.domain.model.ReminderResult
import com.example.androidpractices.feature.profile.domain.repository.ReminderScheduler

class ScheduleClassReminderUseCase(private val scheduler: ReminderScheduler) {
    operator fun invoke(time: ClassTime, userName: String): ReminderResult =
        scheduler.schedule(time, userName.trim().ifEmpty { "Студент" })
}
