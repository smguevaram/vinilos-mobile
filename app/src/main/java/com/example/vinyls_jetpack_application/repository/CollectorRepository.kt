package com.example.vinyls_jetpack_application.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.database.dao.CollectorDao
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CollectorRepository (val application: Application, val collectorsDao: CollectorDao) {

    suspend fun refreshCollectors(): List<Collector> {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val collectorsFromNetwork: List<Collector>? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getCollectors(
                    { collectors ->
                        continuation.resume(collectors)
                    },
                    { error ->
                        continuation.resume(null)
                    }
                )
            }

            collectorsFromNetwork?.let { collectors ->
                withContext(Dispatchers.IO) {
                    collectorsDao.insert(collectors)

                }
                return collectors
            } ?: return emptyList()
        } else {
            return withContext(Dispatchers.IO) {
                collectorsDao.getAllCollectors() ?: emptyList()
            }
        }
    }


}