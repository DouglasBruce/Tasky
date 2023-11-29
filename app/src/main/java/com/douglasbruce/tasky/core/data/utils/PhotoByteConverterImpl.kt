package com.douglasbruce.tasky.core.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.douglasbruce.tasky.core.domain.utils.PhotoByteConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class PhotoByteConverterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PhotoByteConverter {
    override suspend fun uriToBytes(uri: Uri): ByteArray {
        return withContext(Dispatchers.IO) {
            val bitmap = bitmapFromUri(uri)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        }
    }

    private suspend fun bitmapFromUri(uri: Uri): Bitmap {
        return withContext(Dispatchers.IO) {
            val bytes = context.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            } ?: byteArrayOf()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}