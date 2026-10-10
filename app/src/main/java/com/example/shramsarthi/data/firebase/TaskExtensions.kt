package com.example.shramsarthi.data.firebase

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Await extension for Google Play Services Task<T> without external dependency.
 */
suspend fun <T> Task<T>.awaitTask(): T {
    if (isComplete) {
        val e = exception
        return if (e == null) {
            @Suppress("UNCHECKED_CAST")
            result as T
        } else {
            throw e
        }
    }
    return suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (continuation.isActive) {
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    val exception = task.exception ?: Exception("Unknown Firebase Task error")
                    if (exception is CancellationException) {
                        continuation.cancel(exception)
                    } else {
                        continuation.resumeWithException(exception)
                    }
                }
            }
        }
    }
}
