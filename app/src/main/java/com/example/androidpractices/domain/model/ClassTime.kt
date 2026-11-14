package com.example.androidpractices.domain.model

import java.util.Locale

/** Время любимой пары в формате HH:mm (24 часа). */
data class ClassTime(val hour: Int, val minute: Int) {

    override fun toString(): String = "%02d:%02d".format(Locale.ROOT, hour, minute)

    companion object {
        private val PATTERN = Regex("([01]\\d|2[0-3]):([0-5]\\d)")

        /** Возвращает null, если строка пустая или не похожа на время HH:mm (например, "9:5", "25:00", "abc"). */
        fun parse(text: String): ClassTime? {
            val match = PATTERN.matchEntire(text.trim()) ?: return null
            return ClassTime(
                hour = match.groupValues[1].toInt(),
                minute = match.groupValues[2].toInt(),
            )
        }
    }
}
