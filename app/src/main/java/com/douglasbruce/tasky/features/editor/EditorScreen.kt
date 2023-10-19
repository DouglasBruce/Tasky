package com.douglasbruce.tasky.features.editor

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.component.TaskyCenterAlignedTopAppBar
import com.douglasbruce.tasky.core.designsystem.component.TaskyTopAppBarTextButton
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.Green
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.TaskyTheme
import com.douglasbruce.tasky.features.editor.form.EditorEvent
import com.douglasbruce.tasky.features.editor.form.EditorState

@Composable
internal fun EditorRoute(
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = hiltViewModel(),
) {
    EditorScreen(
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        editorUiState = viewModel.state,
        onEvent = viewModel::onEvent,
        modifier = modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun EditorScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit,
    editorUiState: EditorState,
    onEvent: (EditorEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TaskyCenterAlignedTopAppBar(
                title = if (editorUiState.isTitleEditor) stringResource(R.string.edit_title) else stringResource(
                    R.string.edit_description
                ),
                navigationIcon = TaskyIcons.ChevronLeft,
                navigationIconContentDescription = stringResource(R.string.navigate_up),
                onNavigationClick = onBackClick,
                actions = {
                    TaskyTopAppBarTextButton(
                        text = stringResource(R.string.save),
                        onClick = { onSaveClick(editorUiState.key, editorUiState.value) },
                        color = Green,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    navigationIconContentColor = Black,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    actionIconContentColor = Black
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.secondary,
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
            Divider(color = LightBlueVariant)
            BasicTextField(
                value = editorUiState.value,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                onValueChange = { onEvent(EditorEvent.OnTextValueChanged(it)) },
                textStyle = TextStyle(
                    color = Black,
                    fontSize = 16.sp,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditorPreview() {
    TaskyTheme {
        EditorScreen(
            onBackClick = {},
            onSaveClick = { _: String, _: String -> },
            editorUiState = EditorState(key = "", value = "Title", isTitleEditor = true),
            onEvent = {},
        )
    }
}