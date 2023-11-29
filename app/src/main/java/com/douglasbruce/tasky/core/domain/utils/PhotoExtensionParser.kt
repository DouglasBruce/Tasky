package com.douglasbruce.tasky.core.domain.utils

import android.net.Uri

interface PhotoExtensionParser {
    suspend fun extensionFromUri(uri: Uri): String?
}