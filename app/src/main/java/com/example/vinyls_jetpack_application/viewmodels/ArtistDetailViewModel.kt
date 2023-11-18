package com.example.vinyls_jetpack_application.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import com.example.vinyls_jetpack_application.repository.ArtistRepository
import kotlinx.coroutines.launch

class ArtistDetailViewModel(application: Application, artistId: Int, private val artistsDao: ArtistDao) :  AndroidViewModel(application)  {

    private val _artist= MutableLiveData<Artist>();

    private val _artistRepository = ArtistRepository(application, artistsDao)

    val artist: MutableLiveData<Artist>
        get() = _artist

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = artistId


    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: Artist? = _artistRepository.getArtist(id)

            if(data != null) {
                _artist.postValue(data)
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

    class Factory(val app: Application, val artistId: Int, private val artistsDao: ArtistDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(app, artistId, artistsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}