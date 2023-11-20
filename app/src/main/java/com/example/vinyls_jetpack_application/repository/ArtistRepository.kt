package com.example.vinyls_jetpack_application.repository

import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

class ArtistRepository (val application: Application, val artistsDao: ArtistDao){

    suspend fun refreshArtists(): List<Artist> {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val artistsFromNetwork: List<Artist>? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getArtists(
                    { artists ->
                        continuation.resume(artists)
                    },
                    { error ->
                        continuation.resume(null)
                    }
                )
            }

            artistsFromNetwork?.let { artists ->
                withContext(Dispatchers.IO) {
                    artistsDao.insertAll(artists)

                }
                return artists
            } ?: return emptyList()
        } else {
            return withContext(Dispatchers.IO) {
                artistsDao.getAllArtists()
            }
        }
    }


    suspend fun getArtist(id: Int): Artist? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val artistFromNetwork: Artist? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getArtist(id,
                    { artist ->
                        continuation.resume(artist)
                    },
                    {
                        continuation.resume(null)
                    }
                )
            }

            artistFromNetwork?.let { artist ->
                withContext(Dispatchers.IO) {
                    artistsDao.insert(artist)
                }
                return artist
            }
        }
        return withContext(Dispatchers.IO) {
            artistsDao.getArtistById(id)
        }
    }

}