package com.example.androidpractices.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidpractices.model.Film

private val posterPalettes = listOf(
    listOf(Color(0xFF3949AB), Color(0xFF9FA8DA)),
    listOf(Color(0xFF00897B), Color(0xFF80CBC4)),
    listOf(Color(0xFFD84315), Color(0xFFFFAB91)),
    listOf(Color(0xFF6A1B9A), Color(0xFFCE93D8)),
    listOf(Color(0xFF2E7D32), Color(0xFFA5D6A7)),
    listOf(Color(0xFF00838F), Color(0xFF80DEEA)),
)

/**
 * Временная замена постера: градиент и первая буква названия.
 * Когда подключим загрузку картинок по posterUrl, заменим этот компонент.
 */
@Composable
fun PosterPlaceholder(film: Film, modifier: Modifier = Modifier) {
    val colors = posterPalettes[film.id.hashCode().mod(posterPalettes.size)]
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.verticalGradient(colors)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = film.title.take(1).uppercase(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}
