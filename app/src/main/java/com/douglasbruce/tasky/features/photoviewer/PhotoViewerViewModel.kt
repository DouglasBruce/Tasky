package com.douglasbruce.tasky.features.photoviewer

import android.os.Parcelable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.features.photoviewer.navigation.PhotoViewerArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val photoViewerArgs: PhotoViewerArgs = PhotoViewerArgs(savedStateHandle)

    var state by savedStateHandle.saveable {
        mutableStateOf(
            PhotoViewerState(
                url = photoViewerArgs.url,
            )
        )
    }
        private set
}

@Parcelize
data class PhotoViewerState(
    val url: String,
) : Parcelable