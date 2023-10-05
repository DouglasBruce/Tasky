package com.douglasbruce.tasky.core.domain.utils

interface JsonSerializer {
    fun <T> fromJson(json: String, type: Class<T>): T?
}