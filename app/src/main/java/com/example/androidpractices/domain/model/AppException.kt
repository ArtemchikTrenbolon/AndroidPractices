package com.example.androidpractices.domain.model

/** Ошибки, которые понимает остальное приложение (слой данных переводит в них сетевые исключения). */
sealed class AppException(cause: Throwable? = null) : Exception(cause) {
    /** Нет интернета, таймаут и другие проблемы соединения. */
    class NoInternet(cause: Throwable? = null) : AppException(cause)

    /** Сервер ответил ошибкой (5xx, 4xx кроме 404). */
    class Server(val code: Int) : AppException()

    /** Запрошенного ресурса нет (404). */
    class NotFound : AppException()

    /** Ссылка пустая или не является корректным http(s)-адресом. */
    class InvalidUrl : AppException()

    class Unknown(cause: Throwable? = null) : AppException(cause)
}
