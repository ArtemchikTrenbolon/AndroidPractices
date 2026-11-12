package com.example.androidpractices.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidpractices.ui.common.LoadingContent
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

/** Варианты query-параметра limit: (значение для сервера, подпись). */
private val limitOptions = listOf(5 to "5", 10 to "10", 50 to "Все")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSettingsScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilterSettingsViewModel = koinViewModel(),
) {
    val draft by viewModel.draft.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { innerPadding ->
        val current = draft
        if (current == null) {
            LoadingContent(Modifier.padding(innerPadding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                OutlinedTextField(
                    value = current.titleQuery,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Название содержит") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Column {
                    Text(
                        text = "Минимальная оценка: ${current.minScore}",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Slider(
                        value = current.minScore.toFloat(),
                        onValueChange = { viewModel.onMinScoreChange(it.roundToInt()) },
                        valueRange = 0f..100f,
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Сколько фильмов загружать",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        limitOptions.forEach { (value, label) ->
                            FilterChip(
                                selected = current.limit == value,
                                onClick = { viewModel.onLimitChange(value) },
                                label = { Text(label) },
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = viewModel::reset,
                        modifier = Modifier.weight(1f),
                    ) { Text("Сбросить") }
                    Button(
                        onClick = { viewModel.save(onDone) },
                        modifier = Modifier.weight(1f),
                    ) { Text("Готово") }
                }
            }
        }
    }
}
