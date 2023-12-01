package com.example.vinyls_jetpack_application.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.vinyls_jetpack_application.database.dao.AlbumDao
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Comment
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AlbumRepository (val application: Application,  val albumsDao: AlbumDao) {

    suspend fun refreshAlbums(): List<Album> {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val albumsFromNetwork: List<Album>? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getAlbums(
                    { albums ->
                        continuation.resume(albums.sortedByDescending { it.id })
                    },
                    { error ->
                        continuation.resume(null)
                    }
                )
            }

            albumsFromNetwork?.let { albums ->
                withContext(Dispatchers.IO) {
                    albumsDao.insertAll(albums)

                }
                return albums
            } ?: return emptyList()
        } else {
            return withContext(Dispatchers.IO) {
                albumsDao.getAllAlbums()
            }
        }
    }

    suspend fun getAlbum(id: Int): Album? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val albumFromNetwork: Album? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getAlbum(id,
                    { album ->
                        continuation.resume(album)
                    },
                    {
                        continuation.resume(null)
                    }
                )
            }

            albumFromNetwork?.let { album ->
                withContext(Dispatchers.IO) {
                    albumsDao.insert(album)
                }
                return album
            }
        }
        return withContext(Dispatchers.IO) {
            albumsDao.getAlbumById(id)
        }
    }

    suspend fun getCommentsByAlbumId(id: Int): List<Comment>? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if (isConnected) {
            val commentsFromNetwork: List<Comment>? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).getComments(id,
                    { comments ->
                        continuation.resume(comments)
                    },
                    {
                        continuation.resume(null)
                    }
                )
            }

            commentsFromNetwork?.let { comments ->
                return comments
            }
        }
        return listOf()
    }

    suspend fun addAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String): Album? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if(isConnected) {
            val albumToSend = Album(0, name, cover, releaseDate, description, genre, recordLabel)
            val album = JSONObject()
            album.put("name", albumToSend.name)
            album.put("cover", albumToSend.cover)
            album.put("releaseDate", albumToSend.releaseDate)
            album.put("description", albumToSend.description)
            album.put("genre", albumToSend.genre)
            album.put("recordLabel", albumToSend.recordLabel)
            val albumFromNetwork: Album? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).addAlbum(album,
                    { album ->
                        continuation.resume(album)
                    }
                ) {
                    continuation.resume(null)
                }
            }

            albumFromNetwork?.let { album ->
                withContext(Dispatchers.IO) {
                    albumsDao.insert(album)
                }
                return album
            }

        }
        return null
    }

    suspend fun addCommentToAlbum(albumId: Int, comment: String, rating: Int): Comment? {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo?.isConnected == true

        if(isConnected) {
            val commentToSend = Comment(0, comment, rating, "test")
            val commentJson = JSONObject()
            commentJson.put("description", commentToSend.description)
            commentJson.put("rating", commentToSend.rating)
            commentJson.put("collector", "{\"id\":100}")

            val commentFromNetwork: Comment? = suspendCoroutine { continuation ->
                NetworkServiceAdapter.getInstance(application).postComment(commentJson, albumId,
                    { comment ->
                        continuation.resume(comment)
                    }
                ) {
                    continuation.resume(null)
                }
            }

            commentFromNetwork?.let { comment ->
                return comment
            }

        }
        return null
    }
}