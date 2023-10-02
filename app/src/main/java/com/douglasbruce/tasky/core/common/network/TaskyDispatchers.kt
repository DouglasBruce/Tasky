package com.douglasbruce.tasky.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val taskyDispatcher: TaskyDispatchers)

enum class TaskyDispatchers {
    Default,
    IO,
}