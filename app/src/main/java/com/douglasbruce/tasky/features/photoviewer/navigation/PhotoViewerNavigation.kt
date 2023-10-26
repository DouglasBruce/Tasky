package com.douglasbruce.tasky.features.photoviewer.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.photoviewer.PhotoViewerRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val photoViewerNavigationRoute = "photo-viewer"
private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val photoViewerUrlArg = "photoViewerUrl"

internal class PhotoViewerArgs(val url: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(URLDecoder.decode(checkNotNull(savedStateHandle[photoViewerUrlArg]), urlCharacterEncoding))
}

fun NavController.navigateToPhotoViewer(url: String) {
    val encodedUrl = URLEncoder.encode(url, urlCharacterEncoding)
    this.navigate("$photoViewerNavigationRoute/$encodedUrl") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.photoViewerScreen(
    onBackClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit
) {
    composable(
        route = "$photoViewerNavigationRoute/{$photoViewerUrlArg}",
        arguments = listOf(
            navArgument(photoViewerUrlArg) { type = NavType.StringType },
        ),
    ) {
        PhotoViewerRoute(
            onBackClick = onBackClick,
            onRemovePhotoClick = onRemovePhotoClick,
        )
    }
}