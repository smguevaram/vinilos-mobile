package com.example.vinyls_jetpack_application.repository

import android.app.Application
import android.util.Log
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ArtistRepository (val application: Application){

    suspend fun refreshArtists(): List<Artist>? {
        return suspendCoroutine { continuation ->
            NetworkServiceAdapter.getInstance(application).getArtists(
                { artists ->
                    continuation.resume(artists)
                },
                {
                    continuation.resume(null)
                }
            )
        }
    }

    suspend fun getArtist(id: Int): Artist? {
        return suspendCoroutine { continuation ->
            NetworkServiceAdapter.getInstance(application).getArtist(id,
                { artist ->
                    continuation.resume(artist)
                },
                {
                    continuation.resume(null)
                }
            )
        }
    }
}