package com.douglasbruce.tasky.core.data.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.douglasbruce.tasky.core.domain.utils.PhotoExtensionParser
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class PhotoExtensionParserImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PhotoExtensionParser {
    override suspend fun extensionFromUri(uri: Uri): String? {
        return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            uri.path?.let {
                MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString())
            }
        }
    }
}