package com.example.androidpractices.domain.model

/** Настройки поиска и фильтрации списка (не более трёх, как требует задание). */
data class FilmFilter(
    /** Название содержит (ищем и в английском, и в романизированном названии) */
    val titleQuery: String = "",
    /** Минимальная оценка Rotten Tomatoes, 0..100 */
    val minScore: Int = 0,
    /** Сколько фильмов запросить у сервера (query-параметр limit) */
    val limit: Int = DEFAULT_LIMIT,
) {
    /** true, если ничего не отличается от значений по умолчанию (бейдж не нужен). */
    val isDefault: Boolean
        get() = copy(titleQuery = titleQuery.trim()) == FilmFilter()

    /** Название и оценку сервер фильтровать не умеет, поэтому проверяем на устройстве. */
    fun matches(film: Film): Boolean {
        val query = titleQuery.trim()
        val titleMatches = query.isEmpty() ||
            film.title.contains(query, ignoreCase = true) ||
            film.originalTitleRomanised.contains(query, ignoreCase = true)
        return titleMatches && film.rtScore >= minScore
    }

    companion object {
        const val DEFAULT_LIMIT = 10
    }
}
