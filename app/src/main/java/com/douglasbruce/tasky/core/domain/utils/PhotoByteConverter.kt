package com.douglasbruce.tasky.core.domain.utils

import android.net.Uri

interface PhotoByteConverter {
    suspend fun uriToBytes(uri: Uri): ByteArray
}