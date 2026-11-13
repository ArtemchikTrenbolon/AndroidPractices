package com.example.androidpractices.ui.common

import com.example.androidpractices.domain.model.AppException

/** Превращает ошибку из слоя данных в текст для пользователя. */
fun Throwable.toUserMessage(): String = when (this) {
    is AppException.NoInternet -> "Нет подключения к интернету. Проверьте сеть и попробуйте снова."
    is AppException.Server -> "Ошибка сервера (код $code). Попробуйте позже."
    is AppException.NotFound -> "Не найдено."
    is AppException.InvalidUrl -> "Некорректная ссылка. Она должна начинаться с https://"
    else -> "Что-то пошло не так. Попробуйте ещё раз."
}
