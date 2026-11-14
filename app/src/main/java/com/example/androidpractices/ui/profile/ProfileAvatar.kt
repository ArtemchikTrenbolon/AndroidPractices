package com.example.androidpractices.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/** Круглая аватарка. Если фото нет, показываем значок человека. */
@Composable
fun ProfileAvatar(avatarUri: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (avatarUri == null) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Аватар не выбран",
                modifier = Modifier.fillMaxSize(0.6f),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        } else {
            AsyncImage(
                model = avatarUri,
                contentDescription = "Аватар",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
