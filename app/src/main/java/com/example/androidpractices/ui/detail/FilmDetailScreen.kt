package com.example.androidpractices.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.example.androidpractices.domain.model.Film
import com.example.androidpractices.ui.common.ErrorContent
import com.example.androidpractices.ui.common.FilmPoster
import com.example.androidpractices.ui.common.LoadingContent
import com.example.androidpractices.ui.preview.SampleFilms
import com.example.androidpractices.ui.theme.AndroidPracticesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    filmId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilmDetailViewModel = koinViewModel(parameters = { parametersOf(filmId) }),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (state as? FilmDetailUiState.Success)?.film?.title.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (state is FilmDetailUiState.Success) {
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = if (isFavorite) "Убрать из избранного" else "В избранное",
                                tint = if (isFavorite) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        when (val current = state) {
            FilmDetailUiState.Loading -> LoadingContent(Modifier.padding(innerPadding))
            is FilmDetailUiState.Error -> ErrorContent(
                message = current.message,
                onRetry = viewModel::retry,
                modifier = Modifier.padding(innerPadding),
            )
            is FilmDetailUiState.Success -> FilmDetailContent(
                film = current.film,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

/**
 * Сложная разметка на ConstraintLayout: постер слева, заголовки справа от него,
 * ниже строка с цифрами, таблица «подпись — значение» и описание.
 */
@Composable
private fun FilmDetailContent(film: Film, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            val (poster, title, original, romanised, rating, stats) = createRefs()
            val (labelDirector, valueDirector, labelProducer, valueProducer) = createRefs()
            val (divider, descTitle, description) = createRefs()

            // Нижняя граница блока «постер + заголовки»
            val headerBottom = createBottomBarrier(poster, rating, margin = 24.dp)

            FilmPoster(
                film = film,
                modifier = Modifier
                    .size(width = 140.dp, height = 200.dp)
                    .constrainAs(poster) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            )

            Text(
                text = film.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(poster.top)
                    start.linkTo(poster.end, margin = 16.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
            Text(
                text = film.originalTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(original) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
            Text(
                text = film.originalTitleRomanised,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(romanised) {
                    top.linkTo(original.bottom, margin = 2.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.constrainAs(rating) {
                    top.linkTo(romanised.bottom, margin = 12.dp)
                    start.linkTo(title.start)
                },
            ) {
                Text(
                    text = "★ ${film.rtScore} / 100",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                modifier = Modifier.constrainAs(stats) {
                    top.linkTo(headerBottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            ) {
                StatItem(label = "Год выхода", value = film.releaseYear.toString())
                StatItem(label = "Длительность", value = "${film.runningTimeMinutes} мин")
            }

            HorizontalDivider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(stats.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )

            // Таблица «подпись — значение»: значения выровнены по правой границе самой длинной подписи
            val labelsEnd = createEndBarrier(labelDirector, labelProducer, margin = 16.dp)
            val directorRowBottom = createBottomBarrier(labelDirector, valueDirector, margin = 12.dp)

            Text(
                text = "Режиссёр",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(labelDirector) {
                    top.linkTo(divider.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                },
            )
            Text(
                text = film.director,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(valueDirector) {
                    top.linkTo(labelDirector.top)
                    start.linkTo(labelsEnd)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
            Text(
                text = "Продюсер",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.constrainAs(labelProducer) {
                    top.linkTo(directorRowBottom)
                    start.linkTo(parent.start)
                },
            )
            Text(
                text = film.producer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(valueProducer) {
                    top.linkTo(labelProducer.top)
                    start.linkTo(labelsEnd)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )

            val producerRowBottom = createBottomBarrier(labelProducer, valueProducer, margin = 24.dp)
            Text(
                text = "Описание",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(descTitle) {
                    top.linkTo(producerRowBottom)
                    start.linkTo(parent.start)
                },
            )
            Text(
                text = film.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(descTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilmDetailContentPreview() {
    AndroidPracticesTheme {
        FilmDetailContent(film = SampleFilms.castleInTheSky)
    }
}
