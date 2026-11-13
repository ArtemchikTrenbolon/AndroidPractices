package com.example.androidpractices.ui.navigation

object Routes {
    const val FILMS = "films"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val PROFILE_EDIT = "profile/edit"

    const val ARG_FILM_ID = "filmId"
    const val FILM_DETAIL = "film/{$ARG_FILM_ID}"

    fun filmDetail(filmId: String) = "film/$filmId"
}
