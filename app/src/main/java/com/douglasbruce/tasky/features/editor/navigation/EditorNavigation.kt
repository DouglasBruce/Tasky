package com.douglasbruce.tasky.features.editor.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.editor.EditorRoute

const val editorNavigationRoute = "editor"

@VisibleForTesting
internal const val editorTypeArg = "editorType"

@VisibleForTesting
internal const val editorKeyArg = "editorKey"

@VisibleForTesting
internal const val editorValueArg = "editorValue"

internal class EditorArgs(val editorType: Boolean, val editorKey: String, val editorValue: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                checkNotNull(savedStateHandle[editorTypeArg]),
                checkNotNull(savedStateHandle[editorKeyArg]),
                checkNotNull(savedStateHandle[editorValueArg])
            )
}

fun NavController.navigateToEditor(isTitle: Boolean, key: String, value: String) {
    this.navigate("$editorNavigationRoute/$isTitle/$key/$value") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.editorScreen(
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit,
) {
    composable(
        route = "$editorNavigationRoute/{$editorTypeArg}/{$editorKeyArg}/{$editorValueArg}",
        arguments = listOf(
            navArgument(editorTypeArg) {
                type = NavType.BoolType
            },
            navArgument(editorKeyArg) {
                type = NavType.StringType
            },
            navArgument(editorValueArg) {
                type = NavType.StringType
            },
        ),
    ) {
        EditorRoute(
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )
    }
}