package com.example.androidpractices.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/** Вкладки нижней навигации. Две последние пока заглушки. */
enum class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    Films(Routes.FILMS, "Фильмы", Icons.Filled.Home),
    Favorites(Routes.FAVORITES, "Избранное", Icons.Filled.Favorite),
    Profile(Routes.PROFILE, "Профиль", Icons.Filled.Person),
}
