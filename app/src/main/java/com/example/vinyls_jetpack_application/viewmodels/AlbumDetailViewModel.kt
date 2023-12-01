package com.example.vinyls_jetpack_application.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinyls_jetpack_application.database.dao.AlbumDao
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Comment
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import com.example.vinyls_jetpack_application.repository.AlbumRepository
import com.example.vinyls_jetpack_application.repository.ArtistRepository
import kotlinx.coroutines.launch

class AlbumDetailViewModel(application: Application, albumId: Int, albumsDao: AlbumDao) :  AndroidViewModel(application)  {

    private val _album= MutableLiveData<Album>();
    private val _comments = MutableLiveData<List<Comment>>()

    private val _albumRepository = AlbumRepository(application, albumsDao)

    private val _eventCommentAdded = MutableLiveData<Boolean>(false)

    val eventCommentAdded: LiveData<Boolean>
        get() = _eventCommentAdded

    val album: MutableLiveData<Album>
        get() = _album

    val comments: MutableLiveData<List<Comment>>
        get() = _comments

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = albumId


    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: Album? = _albumRepository.getAlbum(id)
            var commentsFromBack: List<Comment>? = _albumRepository.getCommentsByAlbumId(id)

            if(data != null) {
                _album.postValue(data)
                _comments.postValue(commentsFromBack)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } else {
                _eventNetworkError.value = true
            }
        }
    }

    fun addComment(albumId: Int, description: String, rating: Int) {
        viewModelScope.launch {
            var commentAddedSuccessfully: Comment? = _albumRepository.addCommentToAlbum(albumId, description, rating)

            if (commentAddedSuccessfully is Comment) {
                _eventCommentAdded.value = true
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } else {
                _eventNetworkError.value = true
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val albumId: Int, val albumsDao: AlbumDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(app, albumId, albumsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}