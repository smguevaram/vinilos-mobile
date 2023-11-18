package com.example.vinyls_jetpack_application.viewmodels

import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.repository.ArtistRepository
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application, private val artistsDao: ArtistDao) :  AndroidViewModel(application) {

    private val _artists = MutableLiveData<List<Artist>>()

    val artists: LiveData<List<Artist>>
        get() = _artists

    val _artistsRepository = ArtistRepository(application, artistsDao)

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
            var data: List<Artist>? = _artistsRepository.refreshArtists()

            if(data != null) {
                _artists.postValue(data)
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

    class Factory(val app: Application, private val artistsDao: ArtistDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(app, artistsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
