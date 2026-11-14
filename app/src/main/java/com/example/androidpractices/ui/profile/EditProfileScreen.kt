package com.example.androidpractices.ui.profile

import android.Manifest
import android.app.TimePickerDialog
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidpractices.domain.model.ClassTime
import com.example.androidpractices.ui.common.LoadingContent
import org.koin.androidx.compose.koinViewModel

/**
 * Разрешения:
 * - при входе на экран запрашиваем доступ к фото; если пользователь отказал, закрываем экран;
 * - доступ к камере запрашиваем в момент выбора «Камера»;
 * - разрешение на уведомления (Android 13+) запрашиваем при нажатии «Готово».
 * Галерея открывается через системный Photo Picker, снимок делает системное приложение камеры (TakePicture + FileProvider).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val draft by viewModel.draft.collectAsStateWithLifecycle()
    val classTimeTouched by viewModel.classTimeTouched.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showSourceDialog by rememberSaveable { mutableStateOf(false) }
    // Адрес снимка нужно пережить пересоздание Activity, пока открыта камера
    var cameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var storageGranted by rememberSaveable { mutableStateOf(context.hasStorageAccess()) }

    // 1. Доступ к хранилищу (стандартный диалог). Отказ означает выход с экрана.
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        if (result.values.any { it }) {
            storageGranted = true
        } else {
            Toast.makeText(
                context,
                "Без доступа к фото редактирование профиля недоступно",
                Toast.LENGTH_LONG,
            ).show()
            onDone()
        }
    }
    LaunchedEffect(Unit) {
        if (!storageGranted) storagePermissionLauncher.launch(storagePermissions())
    }

    // 2. Галерея
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.onAvatarPicked(uri.toString())
    }

    // 3. Камера: запрос CAMERA, затем съёмка
    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val uri = cameraUri
        if (success && uri != null) viewModel.onAvatarPicked(uri.toString())
    }
    val launchCamera = {
        val uri = context.createCameraUri()
        cameraUri = uri
        takePicture.launch(uri)
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(context, "Для съёмки нужен доступ к камере", Toast.LENGTH_LONG).show()
        }
    }

    // 4. Сохранение: показываем итог установки напоминания и закрываем экран
    val finishSave = {
        viewModel.save { message ->
            if (message != null) Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            onDone()
        }
    }
    // С Android 13 для показа уведомлений нужно разрешение. Просим его перед сохранением.
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Уведомления отключены: напоминание не будет показано", Toast.LENGTH_LONG).show()
        }
        finishSave()
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Фото профиля") },
            text = {
                Column {
                    TextButton(onClick = {
                        showSourceDialog = false
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) { Text("Галерея") }
                    TextButton(onClick = {
                        showSourceDialog = false
                        if (context.hasCameraAccess()) {
                            launchCamera()
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) { Text("Камера") }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { showSourceDialog = false }) { Text("Отмена") } },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { innerPadding ->
        val current = draft
        if (!storageGranted || current == null) {
            LoadingContent(Modifier.padding(innerPadding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ProfileAvatar(
                    avatarUri = current.avatarUri,
                    modifier = Modifier
                        .size(140.dp)
                        .clickable { showSourceDialog = true },
                )
                Text(
                    text = "Нажмите на фото, чтобы изменить",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    value = current.fullName,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("ФИО") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = current.position,
                    onValueChange = viewModel::onPositionChange,
                    label = { Text("Должность") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = current.resumeUrl,
                    onValueChange = viewModel::onResumeUrlChange,
                    label = { Text("Ссылка на резюме (https://...)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth(),
                )
                val parsedTime = ClassTime.parse(current.classTime)
                val timeValid = parsedTime != null
                val showTimeError = classTimeTouched && !timeValid
                OutlinedTextField(
                    value = current.classTime,
                    onValueChange = viewModel::onClassTimeChange,
                    label = { Text("Время любимой пары") },
                    singleLine = true,
                    isError = showTimeError,
                    supportingText = {
                        Text(
                            if (showTimeError) "Введите время в формате ЧЧ:мм, например 09:30"
                            else "Формат ЧЧ:мм, например 09:30",
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            // Окно с часами. При открытии подставляем уже введённое время
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> viewModel.onClassTimeChange(ClassTime(hour, minute).toString()) },
                                parsedTime?.hour ?: 9,
                                parsedTime?.minute ?: 0,
                                true,
                            ).show()
                        }) {
                            Icon(ClockIcon, contentDescription = "Выбрать время на часах")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = {
                        if (context.hasNotificationAccess()) {
                            finishSave()
                        } else {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    enabled = timeValid,
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Готово") }
            }
        }
    }
}
