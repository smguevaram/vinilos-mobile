package com.example.vinyls_jetpack_application.repository

import android.app.Application
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CollectorRepository (val application: Application) {

    suspend fun refreshCollectors(): List<Collector>? {
        return suspendCoroutine { continuation ->
            NetworkServiceAdapter.getInstance(application).getCollectors(
                { collectors ->
                    continuation.resume(collectors)
                },
                {
                    continuation.resume(null)
                }
            )
        }
    }
}