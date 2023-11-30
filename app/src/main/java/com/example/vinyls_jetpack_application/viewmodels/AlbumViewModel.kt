package com.example.vinyls_jetpack_application.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_jetpack_application.database.dao.AlbumDao
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import com.example.vinyls_jetpack_application.repository.AlbumRepository
import com.example.vinyls_jetpack_application.repository.ArtistRepository
import kotlinx.coroutines.launch

class AlbumViewModel(application: Application, albumsDao: AlbumDao) :  AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()

    val albums: LiveData<List<Album>>
        get() = _albums

    val _albumsRepository = AlbumRepository(application, albumsDao)

    private val _eventAlbumCreated = MutableLiveData<Boolean>(false)

    val eventAlbumCreated: LiveData<Boolean>
        get() = _eventAlbumCreated

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: List<Album>? = _albumsRepository.refreshAlbums()

            if(data != null) {
                _albums.postValue(data)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } else {
                _eventNetworkError.value = true
            }
        }
    }

    fun addAlbum(name: String, cover: String, releaseDate: String, description: String, genre: String, recordLabel: String) {
        viewModelScope.launch {
            val success: Album? = _albumsRepository.addAlbum(name, cover, releaseDate, description, genre, recordLabel)

            if (success is Album) {
                _eventAlbumCreated.value = true
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

    class Factory(val app: Application, val albumsDao: AlbumDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app, albumsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
