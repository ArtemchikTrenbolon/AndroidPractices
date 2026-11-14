package com.example.androidpractices.ui.films

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidpractices.ui.common.ErrorContent
import com.example.androidpractices.ui.common.FilmCard
import com.example.androidpractices.ui.common.LoadingContent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsScreen(
    onFilmClick: (String) -> Unit,
    onOpenFilters: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilmsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val filterActive by viewModel.isFilterActive.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Фильмы Studio Ghibli") },
                actions = {
                    IconButton(onClick = onOpenFilters) {
                        BadgedBox(badge = { if (filterActive) Badge() }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Фильтры")
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val current = state) {
                FilmsUiState.Loading -> LoadingContent()
                is FilmsUiState.Error -> ErrorContent(
                    message = current.message,
                    onRetry = viewModel::retry,
                )
                is FilmsUiState.Success -> if (current.films.isEmpty()) {
                    Text(
                        text = "Ничего не найдено. Измените фильтры.",
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(current.films, key = { it.id }) { film ->
                            FilmCard(film = film, onClick = { onFilmClick(film.id) })
                        }
                    }
                }
            }
        }
    }
}
