package com.douglasbruce.tasky.features.photoviewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.TaskyCenterAlignedTopAppBar
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons

@Composable
internal fun PhotoViewerRoute(
    onBackClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PhotoViewerViewModel = hiltViewModel(),
) {
    PhotoViewerScreen(
        onBackClick = onBackClick,
        onRemovePhotoClick = onRemovePhotoClick,
        photoUiState = viewModel.state,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun PhotoViewerScreen(
    onBackClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    photoUiState: PhotoViewerState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TaskyCenterAlignedTopAppBar(
                title = stringResource(R.string.photo),
                navigationIcon = TaskyIcons.Close,
                navigationIconContentDescription = stringResource(R.string.cancel),
                onNavigationClick = onBackClick,
                actions = {
                    IconButton(onClick = { onRemovePhotoClick(photoUiState.url) }) {
                        Icon(
                            imageVector = TaskyIcons.DeleteOutlined,
                            contentDescription = stringResource(R.string.remove_photo)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    )
                ),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUiState.url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.photo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun PhotoViewerScreenPreview() {
    PhotoViewerScreen(
        onBackClick = {},
        onRemovePhotoClick = {},
        photoUiState = PhotoViewerState("")
    )
}