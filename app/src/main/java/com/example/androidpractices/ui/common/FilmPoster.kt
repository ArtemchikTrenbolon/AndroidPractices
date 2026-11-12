package com.example.androidpractices.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.androidpractices.domain.model.Film

/** Постер из сети (Coil). Пока грузится или если не загрузился, показываем цветную заглушку. */
@Composable
fun FilmPoster(film: Film, modifier: Modifier = Modifier) {
    if (film.posterUrl.isNullOrBlank()) {
        PosterPlaceholder(film = film, modifier = modifier)
    } else {
        SubcomposeAsyncImage(
            model = film.posterUrl,
            contentDescription = film.title,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(12.dp)),
            loading = { PosterPlaceholder(film = film, modifier = Modifier.fillMaxSize()) },
            error = { PosterPlaceholder(film = film, modifier = Modifier.fillMaxSize()) },
        )
    }
}
