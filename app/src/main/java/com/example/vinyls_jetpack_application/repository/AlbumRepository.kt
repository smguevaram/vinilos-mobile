package com.example.vinyls_jetpack_application.repository

import android.app.Application
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AlbumRepository (val application: Application) {

    suspend fun refreshAlbums(): List<Album>? {
        return suspendCoroutine { continuation ->
            NetworkServiceAdapter.getInstance(application).getAlbums(
                { albums ->
                    continuation.resume(albums)
                },
                {
                    continuation.resume(null)
                }
            )
        }
    }
}