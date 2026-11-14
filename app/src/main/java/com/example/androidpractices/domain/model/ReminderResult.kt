package com.example.androidpractices.domain.model

/** Итог установки напоминания. */
data class ReminderResult(
    /** Время сегодня уже прошло, поэтому напоминание перенесено на завтра. */
    val isNextDay: Boolean,
    /** Будильник точный. false, если у приложения нет права на точные будильники. */
    val isExact: Boolean,
)
