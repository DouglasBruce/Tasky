package com.douglasbruce.tasky.core.designsystem.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.designsystem.icon.TaskyIcons
import com.douglasbruce.tasky.core.designsystem.theme.Black
import com.douglasbruce.tasky.core.designsystem.theme.Gray
import com.douglasbruce.tasky.core.designsystem.theme.LightBlueVariant
import com.douglasbruce.tasky.core.designsystem.theme.LightGrayBlue
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaPhoto

@Composable
fun PhotoSelector(
    photos: List<AgendaPhoto>,
    onPhotoClick: (AgendaPhoto) -> Unit,
    onPhotosSelected: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    val maxItems = AgendaItem.Event.MAX_PHOTO_AMOUNT - photos.size
    val photoPickerLauncher = when (maxItems > 1) {
        true -> {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxItems),
                onResult = { uris -> onPhotosSelected(uris) }
            )
        }

        false -> {
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = { uri -> onPhotosSelected(listOfNotNull(uri)) }
            )
        }
    }

    if (photos.isEmpty()) {
        EmptyPhotoPicker(
            onAddPhoto = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            isReadOnly = isReadOnly,
            modifier = modifier,
        )
    } else {
        PhotoViewer(
            photos = photos,
            onPhotoClick = { onPhotoClick(it) },
            onAddPhoto = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            isReadOnly = isReadOnly,
            modifier = modifier,
        )
    }
}

@Composable
private fun EmptyPhotoPicker(
    onAddPhoto: () -> Unit,
    isReadOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(color = LightBlueVariant)
            .clickable(enabled = !isReadOnly) { onAddPhoto() },
    ) {
        if (isReadOnly) {
            Text(
                text = stringResource(R.string.no_photos),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Gray,
                )
            )
        } else {
            Icon(
                imageVector = TaskyIcons.Add,
                contentDescription = stringResource(R.string.add_photos),
                tint = Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.add_photos),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Gray,
                )
            )
        }
    }
}

@Composable
private fun PhotoViewer(
    photos: List<AgendaPhoto>,
    onPhotoClick: (AgendaPhoto) -> Unit,
    onAddPhoto: () -> Unit,
    isReadOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = LightBlueVariant),
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.photos),
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight(600),
                    color = Black,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(photos, { photo -> photo.key() }) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable { onPhotoClick(it) }
                            .border(
                                border = BorderStroke(2.dp, color = LightGrayBlue),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clip(shape = RoundedCornerShape(5.dp)),
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.uri())
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = stringResource(R.string.photo),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
                if (photos.size < AgendaItem.Event.MAX_PHOTO_AMOUNT && !isReadOnly) {
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(60.dp)
                                .clickable { onAddPhoto() }
                                .border(
                                    border = BorderStroke(2.dp, color = LightGrayBlue),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                        ) {
                            Icon(
                                imageVector = TaskyIcons.Add,
                                contentDescription = stringResource(R.string.add_photos),
                                tint = LightGrayBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PhotoSelectorPreview() {
    PhotoSelector(
        photos = listOf(AgendaPhoto.Local("", Uri.parse(""))),
        onPhotoClick = {},
        onPhotosSelected = {},
    )
}

@Preview
@Composable
fun EmptyPhotoSelectorPreview() {
    PhotoSelector(
        photos = emptyList(),
        onPhotoClick = {},
        onPhotosSelected = {},
    )
}