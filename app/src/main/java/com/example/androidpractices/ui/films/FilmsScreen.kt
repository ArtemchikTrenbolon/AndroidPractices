package com.example.androidpractices.ui.films

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.ui.common.ErrorContent
import com.example.androidpractices.ui.common.FilmPoster
import com.example.androidpractices.ui.common.LoadingContent
import com.example.androidpractices.ui.preview.SampleFilms
import com.example.androidpractices.ui.theme.AndroidPracticesTheme

/** Варианты query-параметра limit: (значение для сервера, подпись). */
private val limitOptions = listOf(5 to "5", 10 to "10", 50 to "Все")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmsScreen(
    onFilmClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilmsViewModel = viewModel(factory = FilmsViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val limit by viewModel.limit.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Фильмы Studio Ghibli") }) },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LimitSelector(selected = limit, onSelected = viewModel::onLimitSelected)

            Box(modifier = Modifier.weight(1f)) {
                when (val current = state) {
                    FilmsUiState.Loading -> LoadingContent()
                    is FilmsUiState.Error -> ErrorContent(
                        message = current.message,
                        onRetry = viewModel::retry,
                    )
                    is FilmsUiState.Success -> if (current.films.isEmpty()) {
                        Text(
                            text = "Ничего не найдено",
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
}

@Composable
private fun LimitSelector(selected: Int, onSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Показать:", style = MaterialTheme.typography.bodyMedium)
        limitOptions.forEach { (value, label) ->
            FilterChip(
                selected = value == selected,
                onClick = { onSelected(value) },
                label = { Text(label) },
            )
        }
    }
}

@Composable
private fun FilmCard(film: Film, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FilmPoster(film = film, modifier = Modifier.size(width = 80.dp, height = 112.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = film.originalTitleRomanised,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${film.releaseYear} • ${film.director}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "★ ${film.rtScore}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilmCardPreview() {
    AndroidPracticesTheme {
        FilmCard(film = SampleFilms.castleInTheSky, onClick = {})
    }
}
