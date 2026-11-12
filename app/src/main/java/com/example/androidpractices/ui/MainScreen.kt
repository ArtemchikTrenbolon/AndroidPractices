package com.example.androidpractices.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidpractices.ui.common.PlaceholderScreen
import com.example.androidpractices.ui.favorites.FavoritesScreen
import com.example.androidpractices.ui.settings.FilterSettingsScreen
import com.example.androidpractices.ui.detail.FilmDetailScreen
import com.example.androidpractices.ui.films.FilmsScreen
import com.example.androidpractices.ui.navigation.BottomTab
import com.example.androidpractices.ui.navigation.Routes

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Нижняя панель видна только на корневых вкладках, на экране деталей её нет
    val showBottomBar = BottomTab.entries.any { it.route == currentRoute }

    Scaffold(
        // Отступы под системные панели обрабатывают сами экраны и NavigationBar
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.FILMS,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            composable(Routes.FILMS) {
                FilmsScreen(
                    onFilmClick = { filmId ->
                        navController.navigate(Routes.filmDetail(filmId))
                    },
                    onOpenFilters = { navController.navigate(Routes.SETTINGS) },
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onFilmClick = { filmId ->
                        navController.navigate(Routes.filmDetail(filmId))
                    },
                )
            }
            composable(Routes.SETTINGS) {
                FilterSettingsScreen(onDone = { navController.popBackStack() })
            }
            composable(Routes.PROFILE) { PlaceholderScreen(title = "Профиль") }
            composable(
                route = Routes.FILM_DETAIL,
                arguments = listOf(
                    navArgument(Routes.ARG_FILM_ID) { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val filmId = checkNotNull(backStackEntry.arguments?.getString(Routes.ARG_FILM_ID))
                FilmDetailScreen(
                    filmId = filmId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
