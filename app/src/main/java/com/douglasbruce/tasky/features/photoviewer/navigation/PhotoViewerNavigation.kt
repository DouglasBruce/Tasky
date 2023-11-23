package com.douglasbruce.tasky.features.photoviewer.navigation

import androidx.annotation.VisibleForTesting
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.douglasbruce.tasky.features.photoviewer.PhotoViewerRoute
import java.net.URLEncoder

const val photoViewerNavigationRoute = "photo-viewer"
private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val photoViewerKeyArg = "photoViewerKey"

@VisibleForTesting
internal const val photoViewerUriArg = "photoViewerUri"

@VisibleForTesting
internal const val photoViewerCanDeleteArg = "photoViewerCanDelete"

fun NavController.navigateToPhotoViewer(key: String, uri: String, canDelete: Boolean) {
    val encodedUri = URLEncoder.encode(uri, urlCharacterEncoding)
    this.navigate("$photoViewerNavigationRoute/$key/$encodedUri/$canDelete") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.photoViewerScreen(
    onBackClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
) {
    composable(
        route = "$photoViewerNavigationRoute/{$photoViewerKeyArg}/{$photoViewerUriArg}/{$photoViewerCanDeleteArg}",
        arguments = listOf(
            navArgument(photoViewerKeyArg) { type = NavType.StringType },
            navArgument(photoViewerUriArg) { type = NavType.StringType },
            navArgument(photoViewerCanDeleteArg) {
                type = NavType.BoolType
                defaultValue = false
            },
        ),
    ) {
        it.savedStateHandle[photoViewerKeyArg] = it.arguments?.getString(photoViewerKeyArg, "")
        it.savedStateHandle[photoViewerUriArg] = it.arguments?.getString(photoViewerUriArg, "")
        it.savedStateHandle[photoViewerCanDeleteArg] = it.arguments?.getBoolean(photoViewerCanDeleteArg, false)

        PhotoViewerRoute(
            key = it.savedStateHandle.get<String>(photoViewerKeyArg)!!,
            uri = it.savedStateHandle.get<String>(photoViewerUriArg)!!,
            canDelete = it.savedStateHandle.get<Boolean>(photoViewerCanDeleteArg)!!,
            onBackClick = onBackClick,
            onRemovePhotoClick = onRemovePhotoClick,
        )
    }
}